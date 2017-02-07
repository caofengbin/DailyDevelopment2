package cfb.com.dailydevelopment2.example2.progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cfb.com.dailydevelopment2.R;

public class ProgressBarActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar2)
    public ProgressBar mProgressBar2;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = mProgressBar2.getProgress();
            progress = progress + new Random().nextInt(25) + 5;
            mProgressBar2.setProgress(progress);
        }
    };

    private final static int MESSAGE_POST_RESULT = 19089;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        ButterKnife.bind(this);

        new Thread() {
            @Override
            public void run() {
                super.run();
                for(int i = 0 ; i < 10 ; i++) {
                    try {
                        long sleepTime = new Random().nextInt(1000) + 100;
                        Thread.sleep(sleepTime);
                        mHandler.obtainMessage(MESSAGE_POST_RESULT).sendToTarget();
                    } catch(Exception e) {

                    }
                }
            }
        }.start();
    }
}
