package datatransfer.driveevent;

import datatransfer.event.Event;

/** Interface to implement chain of responsibility pattern */
public interface DriveEventHandler {
    void setHandler(DriveEventHandler nextHandler, DriveEventHandler childHandler);
    void process(Event event);
}
