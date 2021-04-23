package datatransfer.executors;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static datatransfer.utils.Preconditions.checkNotNull;

public class ReceiveEventExecutor implements Executor {
    private static final String TAG = ReceiveEventExecutor.class.getSimpleName();
    private final Executor mReceiverEvent;

    public ReceiveEventExecutor() {
        this.mReceiverEvent = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@Nonnull Runnable runnable) {
        checkNotNull(runnable, TAG + " runnable null");
        mReceiverEvent.execute(runnable);
    }
}
