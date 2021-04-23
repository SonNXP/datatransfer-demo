package datatransfer.transmission;

import datatransfer.event.Event;
import datatransfer.external.RtmManager;
import datatransfer.services.RtmService;

import static datatransfer.utils.Preconditions.checkNotNull;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class RtmTransmission {
    private static final String TAG = RtmTransmission.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(RtmTransmission.class);

    private RtmService mRTMService;
    private RtmManager mRtmManager;

    public RtmTransmission(RtmService mRTMService, RtmManager mRtmManager) {
        this.mRTMService = mRTMService;
        this.mRtmManager = mRtmManager;
    }

    public void send(Event event) {
        checkNotNull(event, TAG + " cannot send a null event");
        if (0 < mRtmManager.sendEvent(event)) {
            logger.debug("buildSendingQueue: Receiver forwards Rtm event successfully");
        }
        else {
            logger.debug("buildSendingQueue: Receiver forwards Rtm event fail");
        }
    }

    public void buildSendingQueue(Event event) {
        boolean iRtmDataChecking = true;

        if (event.getTaskID().isEmpty()) {
            iRtmDataChecking = false;
            logger.debug("buildSendingQueue: Rtm event data taskID is null");
        }
        if (event.getPayload().isEmpty()) {
            iRtmDataChecking = false;
            logger.debug("buildSendingQueue: Rtm event data payload is null");
        }
        if (iRtmDataChecking) {
            mRTMService.initService();
            if (mRTMService == null) {
                logger.debug("buildSendingQueue: Cannot get iRTMService");
            }
            else {
                send(event);
            }
        }
    }
}
