package datatransfer.executors;

import java.util.concurrent.Executor;

public class AppExecutor {
    private final Executor receiveEvent;
    private final ScanIntervalExecutor scanInterval;
    private final Executor receiveInterfaceState;
    private final SendReceiverStatusExecutor sendReceiverStatus;

    AppExecutor(Executor receiveEvent, ScanIntervalExecutor scanInterval, Executor receiveInterfaceState,
            SendReceiverStatusExecutor sendReceiverStatus) {
        this.receiveEvent = receiveEvent;
        this.scanInterval = scanInterval;
        this.receiveInterfaceState = receiveInterfaceState;
        this.sendReceiverStatus = sendReceiverStatus;
    }

    public AppExecutor() {
        this(new ReceiveEventExecutor(), new ScanIntervalExecutor(), new ReceiveInterfaceStateExecutor(),
                new SendReceiverStatusExecutor());
    }

    public Executor getReceiveInterfaceState() {
        return receiveInterfaceState;
    }

    public Executor getReceiveEvent() {
        return receiveEvent;
    }

    public ScanIntervalExecutor getScanInterval() {
        return scanInterval;
    }

    public SendReceiverStatusExecutor getSendReceiverStatus() {
        return sendReceiverStatus;
    }

}
