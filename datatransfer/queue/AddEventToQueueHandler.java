package datatransfer.queue;

import datatransfer.event.Event;

public class AddEventToQueueHandler implements QueueHandler {
    /** Not in use this time
    private QueueHandler nextQueueHandler;
    private QueueHandler childQueueHandler;

    public AddEventToQueueHandler(QueueHandler nextQueueHandler, QueueHandler childQueueHandler) {
        this.nextQueueHandler = nextQueueHandler;
        this.childQueueHandler = childQueueHandler;
    }
    */

    @Override
    public void setHandler(QueueHandler nextHandler, QueueHandler childHandler) {
        /* Do nothing */
    }

    @Override
    public void process(Queue<Event> queue, Event event) {
        /* There is enough space for new event so push it into the queue */
        queue.push(event);
    }
}
