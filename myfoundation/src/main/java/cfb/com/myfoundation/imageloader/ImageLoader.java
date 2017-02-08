package cfb.com.myfoundation.imageloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图片加载核心类
 * Created by fegnbincao on 2017/2/5.
 */

public class ImageLoader {

    private static final String TAG = "ImageLoader";

    // 子线程加载图片完成后，发送的消息
    public static final int MESSAGE_POST_RESULT = 1;

    // 定义加载图片的线程池相关的参数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;

    // 定义一个int变量作为tag的key
    private static final int TAG_KEY_IMAGE_URL = 45676787;              // 防止加载图片出现乱序的关键所在
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;       // 磁盘缓存大小
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final int DISK_CACHE_INDEX = 0;
    private boolean mIsDiskLruCacheCreated = false;

    // 自定义的ThreadFactory，在创建线程池的时候使用
    // 主要用来给线程定义name，便于定位问题
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            sThreadFactory
    );

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            // 子线程加载图片完成子啊主线程回调
            ImageView imageView = result.imageView;
            // 这里set两边bitmap的原因讲道理我也没懂？？
            imageView.setImageBitmap(result.bitmap);
            String uri = (String)imageView.getTag(TAG_KEY_IMAGE_URL);
            if(uri.equals(result.uri)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Log.w(TAG, "set image bitmap,but url has changed, ignored!");
            }
            super.handleMessage(msg);
        }
    };

    private Context mContext;
    private ImageResizer mImageResizer = new ImageResizer();
    private LruCache<String,Bitmap> mMemoryCache;                           // 内存缓存
    private DiskLruCache mDiskLruCache;                                     // 磁盘缓存

    // ImageLoader是一个单例的存在
    private ImageLoader(Context context) {
        mContext = context.getApplicationContext();

        // 初始化内存缓存相关
        // 只需要提供缓存对象的大小，并重写sizeOf方法就可以
        // sizeOf方法的作用是计算缓存对象的大小，
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };

        // 初始化磁盘缓存相关
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                /**
                 * open方法各个参数的意义
                 * File directory：缓存文件在文件系统中的存储路径
                 * int appVersion：应用的版本号，实际开发中通常都设置为1，意义不大
                 * int valueCount：单个节点对应的数据的个数，通常设置为1
                 * long maxSize：缓存文件的大小
                 */
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1,
                        DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建全局ImageLoader单例
     * 一个单例模式的实现
     * build a new instance of ImageLoader
     * @param context
     * @return a new instance of ImageLoader
     */
    public static ImageLoader build(Context context) {
        return new ImageLoader(context);
    }

    /**
     * 对外暴露的方法，用来在指定控件上显示图片
     * 调用这种方式会按照原图显示
     * NOTE THAT: should run in UI Thread
     * @param url
     * @param imageView
     */
    public void bindBitmap(final String url,final ImageView imageView) {
        bindBitmap(url, imageView, 0, 0);
    }

    /**
     * 对外暴露的方法，用来在指定控件上显示图片
     * 指定需要显示区域的高度和宽度
     * 是一个异步加载图片的实现过程
     * @param url
     * @param imageView
     * @param reqWidth
     * @param reqHeight
     */
    public void bindBitmap(final String url,final ImageView imageView,final int reqWidth,
                           final int reqHeight) {
        // setTag的过程非常的重要
        imageView.setTag(TAG_KEY_IMAGE_URL,url);
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        // 从磁盘缓存或者网络请求中load图片
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                if(bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView, url, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result).sendToTarget();
                }
            }
        };

        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 加载图片的核心方法
     * load bitmap from memory cache or disk cache or network.
     * @param url http url
     * @param reqWidth the width ImageView desired
     * @param reqHeight the height ImageView desired
     * @return bitmap, maybe null.
     */
    private Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if(bitmap != null) {
            Log.d(TAG,"内存缓存命中图片，url为：" + url);
            return bitmap;
        }

        try {
            bitmap = loadBitmapFromDiskCache(url, reqWidth, reqHeight);
            if(bitmap != null) {
                Log.d(TAG,"磁盘缓存命中图片，url为：" + url);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(url, reqWidth, reqHeight);
            Log.d(TAG, "从网络请求加载图片,url:" + url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 这种情况是为了避免因为磁盘缓存文件创建失败而无法加载图片的问题
        if (bitmap == null && !mIsDiskLruCacheCreated) {
            Log.w(TAG, "encounter error, DiskLruCache is not created.");
            bitmap = downloadBitmapFromUrl(url);
        }

        return bitmap;
    }

    /**
     * 1.从内存缓存中加载图片的方法
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromMemCache(String url) {
        final String key = hashKeyFormUrl(url);
        Bitmap bitmap = getBitmapFromMemCache(key);
        return bitmap;
    }

    /**
     * 2.从磁盘缓存中加载图片的方法
     * 从DiskLruCache读文件主要涉及使用DiskLruCache.Snapshot
     * @param url           图片的url地址
     * @param reqWidth      指定的宽度
     * @param reqHeight     指定的高度
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth,
                                           int reqHeight) throws IOException {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from UI Thread, it's not recommended!");
        }

        if(mDiskLruCache == null) {
            return null;
        }

        Bitmap bitmap = null;
        String key = hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if(snapshot != null) {
            // 通过DiskLruCache.Snapshot对象得到缓存文件的输入流
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            // 这里不通过FileInputStream是有坑的！！详见原书423页的解释
            bitmap = mImageResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor,
                    reqWidth,reqHeight);
            if(bitmap != null) {
                // 从磁盘缓存文件读取成功需要将文件存入内存缓存中
                addBitmapToMemoryCache(key,bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 3.从网络请求加载图片的方法
     * NOTE：这里会先将从网络下载出的图片存入磁盘缓存
     * 而后再从磁盘缓存读取资源
     * @param url               资源url
     * @param reqWidth          指定的宽度
     * @param reqHeight         指定的高度
     * @return
     */
    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight)
            throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        // 如果没有磁盘缓存，该方法load图片失败，执行下一个方法downloadBitmapFromUrl()
        if (mDiskLruCache == null) {
            return null;
        }

        String key = hashKeyFormUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            // 打开磁盘缓存文件的输入流
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url, outputStream)) {
                // 通过commit()才能真真的实现文件写入操作
                editor.commit();
            } else {
                // 下载过程中出现了异常，通过abort()来回退整个操作
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    /**
     * 4.从网络请求加载图片的方法
     * 与方法3：loadBitmapFromHttp的区别在于这个不会将网络流写入磁盘文件
     * 通常在SD等存储系统异常时调用该方法
     * @param urlString     图片的url地址
     * @return
     */
    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (final IOException e) {
            Log.e(TAG, "Error in downloadBitmap: " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            close(in);
        }
        return bitmap;
    }

    /**
     * 方法(3)网络请求加载图片的方法中用到的流程
     * 根据请求的url路径，将指定网络文件流写入到文件系统上
     * @param urlString             指定图片的url
     * @param outputStream          文件系统输入流
     * @return
     */
    public boolean downloadUrlToStream(String urlString,
                                       OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "downloadBitmap failed." + e);
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            close(out);
            close(in);
        }
        return false;
    }

    /**
     * 添加Bitmap到内存缓存
     * 当从磁盘缓存读取到图片时，会将它加入
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key,Bitmap bitmap) {
        if(getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key,bitmap);
        }
    }

    /**
     * 从内存缓存中取图片
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 根据图片的url求key值的过程
     * 将url转化为key值的原因：通常图片的url很可能含有特殊的字符，、
     * 这将影响url在Android中的使用
     * 一般采用图片url的MD5值作为key
     * @param url  带转化为MD5的url值
     * @return     相应uil的MD5值
     */
    private String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 关闭流的方法
     * @param closeable
     */
    private void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义的内部类，表示一个加载图片成功的结果
     * 用于在主线程回调处理UI显示
     */
    private static class LoaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }

    // 磁盘缓存文件的创建
    private File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment
                .getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    // 磁盘缓存文件的扩容
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
}
