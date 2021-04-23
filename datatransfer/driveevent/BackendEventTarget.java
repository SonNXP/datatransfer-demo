package datatransfer.driveevent;

import datatransfer.event.Event;
import datatransfer.mediator.Mediator;

public class BackendEventTarget implements DriveEventHandler {
    private DriveEventHandler nextHandler;
    private DriveEventHandler childHandler;

    private Mediator mediator;

    public BackendEventTarget(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void setHandler(DriveEventHandler nextHandler, DriveEventHandler childHandler) {
        this.nextHandler = nextHandler;
        this.childHandler = childHandler;
    }

    @Override
    public void process(Event event) {
        mediator.storeEvent(event);
    }
}
