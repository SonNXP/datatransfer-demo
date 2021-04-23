package datatransfer.queue;

import datatransfer.event.Event;

public class CheckQueueSizeHandler implements QueueHandler {
    private QueueHandler nextQueueHandler;
    private QueueHandler childQueueHandler;

    public CheckQueueSizeHandler() {
    }

    @Override
    public void setHandler(QueueHandler nextHandler, QueueHandler childHandler) {
        this.nextQueueHandler = nextHandler;
        this.childQueueHandler = childHandler;
    }

    @Override
    public void process(Queue<Event> queue, Event event) {
        int queueSize = queue.getRemainingCapacityInBytes();
        int eventSize = event.getEventLength();

        if (queueSize < eventSize) {
            /** The input queue has NOT enough space for new event, delete the oldest events */
            childQueueHandler.process(queue, event);
        } else {
            /** The input queue has enough space for new event, add this event */
            nextQueueHandler.process(queue, event);
        }
    }
}
