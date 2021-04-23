package datatransfer.external.os;

public class IBinder {
    public interface DeathRecipient {
        void binderDied(IBinder who);
    }
}
