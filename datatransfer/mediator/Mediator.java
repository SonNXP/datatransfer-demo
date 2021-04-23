package datatransfer.mediator;

import datatransfer.event.Event;

public interface Mediator {
    public void receiveEvent(Event event);
    public void driveEvent();
    public void storeEvent(Event event);
    public void sendEventToBackend();
    public void forwardEventToRTM(Event event);

    public void sendStatus();
    public void updateInterfacesStatus();
}
