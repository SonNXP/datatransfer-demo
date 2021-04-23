package datatransfer.queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import datatransfer.mediator.Colleague;

public abstract class Queue<E> implements Colleague {
    /** Maximum queue size in byte */
    protected int queueSizeInBytesMax;
    /** Real the queue size in byte */
    protected int queueSizeInBytes;

    /** Main lock guarding all access */
    protected final ReentrantLock lock;
    /** Condition for waiting takes */
    protected final Condition notEmpty;

    /** Add event to the queue, process adding business logic before push */
    public abstract void add(E event);

    /** Push event directly into the queue */
    protected abstract void push(E event);

    /** Take an event out of the queue */
    public abstract E take() throws InterruptedException;

    /** Delete the oldest event in the queue */
    protected abstract void deleteOldestEvent(int newRequestSpace);

    public Queue() {
        /** Keep producer and consumer threads in order */
        this.lock = new ReentrantLock(true);
        this.notEmpty = lock.newCondition();
    }

    /** Get remaining capacity of the queue in byte */
    protected int getRemainingCapacityInBytes() {
        lock.lock();
        try {
            return queueSizeInBytesMax - queueSizeInBytes;
        } finally {
            lock.unlock();
        }
    }

    /** Check the queue is empty or not */
    public boolean isEmpty() {
        lock.lock();
        try {
            return queueSizeInBytes <= 0;
        } finally {
            lock.unlock();
        }
    }

    /** Subtract an event length from the queue size
     * Only use in thread-safe methods
     */    
    protected boolean decQueueSize(int eventLength) {
        queueSizeInBytes -= eventLength;
        if (queueSizeInBytes < 0) {
            queueSizeInBytes = 0;
            /** Set error code if request */
            return false;
        }
        return true;
    }

    /** Add an event length from the queue size 
     * Only use in thread-safe methods
     */
    protected boolean incQueueSize(int evenLength) {
        queueSizeInBytes += evenLength;
        if (queueSizeInBytes > queueSizeInBytesMax) {
            queueSizeInBytes = queueSizeInBytesMax;
            /** Set error code if request */
            return false;
        }
        return true;
    }
}
