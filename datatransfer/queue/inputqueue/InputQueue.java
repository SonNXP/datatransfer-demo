package datatransfer.queue.inputqueue;

import static datatransfer.event.EventState.eventState_InQueue;
import static datatransfer.event.EventState.eventState_ToClassifying;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datatransfer.event.Event;
import datatransfer.executors.AppExecutor;
import datatransfer.mediator.Mediator;
import datatransfer.queue.AddEventToQueueHandler;
import datatransfer.queue.CheckQueueSizeHandler;
import datatransfer.queue.Queue;
import datatransfer.queue.QueueFullHandler;
import datatransfer.queue.QueueHandler;

public class InputQueue extends Queue<Event> {
    private static final Logger logger = LoggerFactory.getLogger(InputQueue.class);
    private static final int INPUT_QUEUE_SIZE_IN_BYTES_MAX = 10 * 1024;

    /** FIFO queue */
    private final LinkedList<Event> queue = new LinkedList<Event>();

    protected Producer producer;

    protected final AppExecutor appExecutor;

    public InputQueue(AppExecutor appExecutor) {
        this.appExecutor = appExecutor;
        producer = new Producer(this);
        queueSizeInBytes = 0;
        queueSizeInBytesMax = INPUT_QUEUE_SIZE_IN_BYTES_MAX;
    }

    @Override
    public void add(Event event) {
        try {
            /** Put this Runnable into a task queue of ReceiveEventExecutor thread */
            appExecutor.getReceiveEvent().execute(() -> {
                producer.run(event);
            });
        } catch (RejectedExecutionException e) {
            logger.error("Cannot run the producer to add new event ", e);
        }
    }

    @Override
    public Event take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (queueSizeInBytes == 0)
                notEmpty.await();
            /**
             * Retrieves and removes the head of the input queue, O(1) or null if the input
             * queue is empty
             */
            Event event = queue.poll();
            if (event != null) {
                decQueueSize(event.getEventLength());
                event.setEventState(eventState_ToClassifying);
            } else {
                logger.error("Event is null, the input queue is empty");
                /** Set error code if request */
            }
            return event;
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void push(Event event) {
        lock.lock();
        try {
            /** Inserts the event at the tail of this queue, O(1) */
            if (queue.offer(event)) {
                incQueueSize(event.getEventLength());
                event.setEventState(eventState_InQueue);
                notEmpty.signal();
            } else {
                logger.error("Fail to push event into the input queue ");
            }

        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void deleteOldestEvent(int newRequestSpace) {
        /**
         * newRequestSpace parameter doesn't use in this function because this queue
         * unsorted
         */
        lock.lock();
        try {
            /** Time complexity O(n) */
            Event oldestEvent = findOldestEvent();
            /** Removes an event from this queue, O(1) */
            boolean result = queue.remove(oldestEvent);
            if (result) {
                decQueueSize(oldestEvent.getEventLength());
            } else {
                logger.error("Fail to remove event from the input queue ");
                /** Set error code if request */
            }
        } finally {
            lock.unlock();
        }
    }

    private Event findOldestEvent() {
        Event eventNext, oldestEvent;
        /**
         * Linked list: Returns an iterator over the elements in this queue in proper
         * sequence. The elements will be returned in order from first (head) to last
         * (tail).
         */
        Iterator<Event> iterator = queue.iterator();
        oldestEvent = (Event) iterator.next();
        while (iterator.hasNext()) {
            eventNext = (Event) iterator.next();
            /** Oldest event is event with minimum of (timestamp + timeToLive) */
            if (eventNext.getEventExistTime() < oldestEvent.getEventExistTime()) {
                oldestEvent = eventNext;
            }
        }
        return oldestEvent;
    }

    protected class Producer {
        private final Queue<Event> inputQueue;

        private QueueHandler addEventToInputQueueHandler;
        private QueueHandler queueFullHandler;
        private QueueHandler checkQueueSizeHandler;

        public Producer(Queue<Event> q) {
            inputQueue = q;

            addEventToInputQueueHandler = new AddEventToQueueHandler();
            queueFullHandler = new QueueFullHandler();
            checkQueueSizeHandler = new CheckQueueSizeHandler();

            queueFullHandler.setHandler(checkQueueSizeHandler, null);
            checkQueueSizeHandler.setHandler(addEventToInputQueueHandler, queueFullHandler);
        }

        public void run(Event event) {
            checkQueueSizeHandler.process(inputQueue, event);
        }
    }

    @Override
    public void setMediator(Mediator mediator) {
        // TODO Auto-generated method stub
    }
}
