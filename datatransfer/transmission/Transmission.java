package datatransfer.transmission;

import datatransfer.event.Event;

/** 
 * ! TO DO add the transmission business logic
 */
public interface Transmission {
    public void send();
    public void buildSendingQueue(Event event);
}
