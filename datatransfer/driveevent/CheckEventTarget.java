package datatransfer.driveevent;

import datatransfer.event.Event;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class CheckEventTarget implements DriveEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(CheckEventTarget.class);

    private DriveEventHandler nextHandler;
    private DriveEventHandler childHandler;

    @Override
    public void setHandler(DriveEventHandler nextHandler, DriveEventHandler childHandler) {
        this.nextHandler = nextHandler;
        this.childHandler = childHandler;
    }

    @Override
    public void process(Event event) {
        switch (event.getTarget()) {
            case Backend:
            case Backend_Small:
            case Backend_Large:
                nextHandler.process(event);
                break;
            case Backend_RTM:
                childHandler.process(event);
                break;
            default:
                logger.debug("Event's target is not for the receiver " + event.getTarget().toString());
        }
    }
}
