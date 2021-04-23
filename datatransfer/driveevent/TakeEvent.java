package datatransfer.driveevent;

import datatransfer.event.Event;
import datatransfer.queue.inputqueue.InputQueue;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class TakeEvent implements DriveEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(TakeEvent.class);
    private DriveEventHandler nextHandler;
    private InputQueue inputQueue;

    public TakeEvent(InputQueue inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public void setHandler(DriveEventHandler nextHandler, DriveEventHandler childHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void process(Event event) {
        try {
            event = inputQueue.take();
            if (event != null) {
                nextHandler.process(event);
            }
        } catch (InterruptedException e) {
            logger.error("Cannot take event from the input queue.", e);
        }
    }
}
