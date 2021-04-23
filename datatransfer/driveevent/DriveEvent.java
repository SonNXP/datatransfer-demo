package datatransfer.driveevent;

import datatransfer.mediator.Mediator;
import datatransfer.queue.inputqueue.InputQueue;

/**
 * This is main class to classify events, depends on target then store events or
 * send event to RTM backend , run() method will be run in mainthread
 */
public class DriveEvent implements Runnable {
    private final InputQueue inputQueue;
    private final Mediator mediator;

    private final DriveEventHandler backendEventTarget;
    private final DriveEventHandler checkEventTarget;
    private final DriveEventHandler rtmEventTarget;
    private final TakeEvent takeEvent;

    public DriveEvent(InputQueue inputQueue, Mediator mediator) {
        this.inputQueue = inputQueue;
        this.mediator = mediator;

        this.backendEventTarget = new BackendEventTarget(mediator);
        this.checkEventTarget = new CheckEventTarget();
        this.rtmEventTarget = new RtmEventTarget(mediator);
        this.takeEvent = new TakeEvent(inputQueue);

        /** Set business logics for classifying events */
        backendEventTarget.setHandler(null, null);
        checkEventTarget.setHandler(backendEventTarget, rtmEventTarget);
        rtmEventTarget.setHandler(null, null);
        takeEvent.setHandler(checkEventTarget, null);
    }

    public void classifyEvent() {
        /** Take an event from the input queue if have, if not just wait */
        takeEvent.process(null);
    }

    @Override
    public void run() {
        classifyEvent();
    }
}
