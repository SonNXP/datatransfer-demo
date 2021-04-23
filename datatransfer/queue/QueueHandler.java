package datatransfer.queue;

import datatransfer.event.Event;

/** Interface to implement chain of responsibility pattern */
public interface QueueHandler {
    public void setHandler(QueueHandler nextHandler, QueueHandler childHandler);
    public void process(Queue<Event> queue, Event event);
}
