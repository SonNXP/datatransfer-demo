package datatransfer.executors;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static datatransfer.utils.Preconditions.checkNotNull;

public class SendReceiverStatusExecutor {
    private static final String TAG = SendReceiverStatusExecutor.class.getSimpleName();
    private static final int SCAN_INTERVAL_INIT_TIME = 0;
    public static final int SCAN_INTERVAL_FIXED_DELAY = 1;

    private final ScheduledExecutorService mSendReceiverStatus;

    public SendReceiverStatusExecutor() {
        this.mSendReceiverStatus = Executors.newSingleThreadScheduledExecutor();
    }

    public void setupSendReceiverStatusExecutor(@Nonnull Runnable runnable) {
        checkNotNull(runnable, TAG + " runnable null");
        mSendReceiverStatus.scheduleWithFixedDelay(runnable, SCAN_INTERVAL_INIT_TIME, SCAN_INTERVAL_FIXED_DELAY,
                TimeUnit.SECONDS);
    }
}
