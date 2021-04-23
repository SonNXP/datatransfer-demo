package datatransfer.external;

import datatransfer.event.Event;

public class RtmManager {
    public int sendEvent(Event event) {
        return event.getEventLength();
    }
}
