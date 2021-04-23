package datatransfer.queue.outputqueue;

import static datatransfer.event.EventState.eventState_InScanning;
import static datatransfer.event.EventState.eventState_InStorage;

import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datatransfer.event.Event;
import datatransfer.executors.AppExecutor;
import datatransfer.mediator.Mediator;
import datatransfer.queue.Queue;
import datatransfer.queue.inputqueue.InputQueue;

public final class OutputQueue extends InputQueue {
    private static final Logger logger = LoggerFactory.getLogger(OutputQueue.class);
    private static final int OUTPUT_QUEUE_SIZE_IN_BYTES_MAX = 7 * 1024;

    private final PriorityQueue<Event> queue = new PriorityQueue<>();

    public OutputQueue(AppExecutor appExecutor) {
        super(appExecutor);
        queueSizeInBytes = 0;
        queueSizeInBytesMax = OUTPUT_QUEUE_SIZE_IN_BYTES_MAX;
        producer = new Producer(this);
    }

    @Override
    public void add(Event event) {
        producer.run(event);
    }

    
    // TODO: Need Target parameter    
    @Override
    public Event take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            /**
             * Retrieves and removes the head (the highest priority event) of this queue,
             * O(log(n)) or returns null if this queue is empty.
             */
            Event event = queue.poll();
            if (event != null) {
                decQueueSize(event.getEventLength());
                event.setEventState(eventState_InScanning);
            } else {
                logger.warn("Take NULL event from the output queue.");
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
            try {
                /**
                 * Inserts the event into this output queue, O(log(n)) Space for this event
                 * processed by add() method upper.
                 */
                if (queue.add(event)) {
                    incQueueSize(event.getEventLength());
                    event.setEventState(eventState_InStorage);
                } else {
                    logger.error("Fail to push event into the output queue ");
                }
            } catch (ClassCastException e) {
                logger.error("Fail to push event into the output queue ", e);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void deleteOldestEvent(int newRequestSpace) {
        lock.lock();
        try {
            /** Space complexity O(n) */
            PriorityQueue<Event> newQueue = new PriorityQueue<>();
            int newQueueSizeInBytes = 0;
            /**
             * ? ~ O(n*log(n))
             */
            while (true) {
                Event event = queue.poll();
                if ((queueSizeInBytes - newQueueSizeInBytes) <= newRequestSpace) {
                    break;
                }
                newQueue.add(event);
                newQueueSizeInBytes += event.getEventLength();
            }
            /** Depend on remaining event number, ~ O(1) normally */
            queue.clear();
            /** Copy newQueue back to the output queue and update queueSize, O(n*log(n)) */
            while (newQueue.size() > 0) {
                queue.add(newQueue.poll());
            }
            queueSizeInBytes = newQueueSizeInBytes;
        } finally {
            lock.unlock();
        }
    }

    class Consumer {
        private final Queue<Event> outputQueue;

        // TODO business logic for the output queue consumer 
        public Consumer(Queue<Event> q) {
            outputQueue = q;
        }

        public void run(Event event) {
        }
    }

    @Override
    public void setMediator(Mediator mediator) {
        /** this.mediator = mediator; */
    }
}
