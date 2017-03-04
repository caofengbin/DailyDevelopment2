package cfb.com.dailydevelopment2.example7.local.refresh;

/**
 * 基本的消息类型item
 * Created by fengbincao on 2017/3/4.
 */

public class Msg {

    public static final int TYPE_RECEIVED = 0;

    public static final int TYPE_SENT = 1;

    private String content;

    private int type;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
