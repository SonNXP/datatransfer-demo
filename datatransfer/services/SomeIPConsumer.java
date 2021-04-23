package datatransfer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datatransfer.event.Event;
import datatransfer.event.EventState;
import datatransfer.external.BnReceiverSomeIPConsumerReceiver;
import datatransfer.external.os.IInterface;
import datatransfer.mediator.Mediator;

public class SomeIPConsumer extends Service {
    private static final Logger logger = LoggerFactory.getLogger(SomeIPConsumer.class);

    protected Mediator mediator;
    private ReceiverSomeIPConsumerReceiver receiverSomeIPConsumerReceiver;

    public SomeIPConsumer() {
        /** Auto call the base constructor */
        receiverSomeIPConsumerReceiver = new ReceiverSomeIPConsumerReceiver();
    }

    @Override
    public void initService() {
        /** Will do when determine a platform */
    }

    @Override
    public void registerReceiver(IInterface receiver) {
        /** Will do when determine a platform */
    }

    @Override
    public void unregisterReceiver(IInterface receiver) {
        /** Will do when determine a platform */
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    private class ReceiverSomeIPConsumerReceiver extends BnReceiverSomeIPConsumerReceiver {
        /* This is callback function, it is called if there is a new event */
        @Override
        protected void onEventChanged(long timestamp, long[] taskID, int jobID, int target, long timeToLive,
                int transferPriority, int identifierType, int[] payload) {
            Event event = new Event(timestamp, taskID, jobID, target, timeToLive, transferPriority, identifierType,
                    payload);

            if (!event.getEventState().equals(EventState.eventState_Undefine)) {
                mediator.receiveEvent(event);
            } else {
                /* Diagnostic Trouble Code */
                logger.debug("Receive a event but its target does not belong to receiver, invalid target=%d", target);
            }
        }
    }

    /** For demo only */
    public void setEventChange(long timestamp, long[] taskID, int jobID, int target, long timeToLive,
            int transferPriority, int identifierType, int[] payload) {
        receiverSomeIPConsumerReceiver.onEventChanged(timestamp, taskID, jobID, target, timeToLive, transferPriority,
                identifierType, payload);
    }
}
