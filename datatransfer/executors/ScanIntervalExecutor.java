package datatransfer.executors;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static datatransfer.utils.Preconditions.checkNotNull;

public class ScanIntervalExecutor {
    private static final String TAG = ScanIntervalExecutor.class.getSimpleName();

    private static final int SCAN_INTERVAL_INIT_TIME = 0;
    public static final int SCAN_INTERVAL_FIXED_DELAY = 100;

    private final ScheduledExecutorService mScanInterval;

    public ScanIntervalExecutor() {
        this.mScanInterval = Executors.newSingleThreadScheduledExecutor();
    }

    public void setupScanIntervalExecutor(@Nonnull Runnable runnable) {
        checkNotNull(runnable, TAG + " runnable null");
        mScanInterval.scheduleWithFixedDelay(runnable, SCAN_INTERVAL_INIT_TIME, SCAN_INTERVAL_FIXED_DELAY,
                TimeUnit.MILLISECONDS);
    }
}
