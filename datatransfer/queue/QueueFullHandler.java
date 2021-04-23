package datatransfer.queue;

import datatransfer.event.Event;

public class QueueFullHandler implements QueueHandler {
    private QueueHandler nextQueueHandler;

    @Override
    public void setHandler(QueueHandler nextHandler, QueueHandler childHandler) {
        this.nextQueueHandler = nextHandler;
    }

    @Override
    public void process(Queue<Event> queue, Event event) {
        int newRequestSpace = event.getEventLength();
        queue.deleteOldestEvent(newRequestSpace);
        /* After deleting oldest event, recheck queue size */
        nextQueueHandler.process(queue, event);
    }
}
