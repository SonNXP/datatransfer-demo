package datatransfer.executors;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static datatransfer.utils.Preconditions.checkNotNull;

public class ClarifyEventExecutor implements Executor {
    private static final String TAG = ClarifyEventExecutor.class.getSimpleName();
    private final Executor mClarifyEvent;

    public ClarifyEventExecutor() {
        this.mClarifyEvent = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@Nonnull Runnable runnable) {
        checkNotNull(runnable, TAG + " runnable null");
        mClarifyEvent.execute(runnable);
    }
}
