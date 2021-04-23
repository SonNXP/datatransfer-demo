package datatransfer.executors;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static datatransfer.utils.Preconditions.checkNotNull;

public class ReceiveInterfaceStateExecutor implements Executor {
    private static final String TAG = ReceiveInterfaceStateExecutor.class.getSimpleName();
    private final Executor mReceiveInterfaceState;

    public ReceiveInterfaceStateExecutor() {
        this.mReceiveInterfaceState = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@Nonnull Runnable runnable) {
        checkNotNull(runnable, TAG + " runnable null");
        mReceiveInterfaceState.execute(runnable);
    }
}
