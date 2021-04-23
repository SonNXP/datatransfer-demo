package datatransfer.external.os;

import javax.annotation.Nonnull;

public class Handler {
    public Handler() {}
    public Handler(Looper looper) {}
    public void post(Runnable command) {}
    public void handleMessage(@Nonnull Message msg) {}
    public Message obtainMessage(int mess) {
        Message msg = new Message();
        return msg;
    }
    public void sendMessageDelayed(Message msg, int ms) {}
}
