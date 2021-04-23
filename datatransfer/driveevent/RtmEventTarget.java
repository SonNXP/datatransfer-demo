package datatransfer.driveevent;

import datatransfer.event.Event;
import datatransfer.event.EventState;
import datatransfer.mediator.Mediator;

public class RtmEventTarget implements DriveEventHandler {
    private DriveEventHandler nextHandler;
    private DriveEventHandler childHandler;

    private final Mediator mediator;

    public RtmEventTarget(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void setHandler(DriveEventHandler nextHandler, DriveEventHandler childHandler) {
        this.nextHandler = nextHandler;
        this.childHandler = childHandler;
    }

    @Override
    public void process(Event event) {
        event.setEventState(EventState.eventState_ForwardRTM);
        mediator.forwardEventToRTM(event);
    }
}
