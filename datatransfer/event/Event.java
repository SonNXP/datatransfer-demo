package datatransfer.event;

import java.io.Serializable;
import java.util.ArrayList;

import static datatransfer.event.EventState.eventState_InReceiving;
import static datatransfer.event.EventState.eventState_Undefine;
import static datatransfer.event.Target.Backend;
import static datatransfer.event.Target.Backend_Large;
import static datatransfer.event.Target.Backend_Small;
import static datatransfer.event.Target.Backend_RTM;

public class Event extends Object implements Comparable<Event>, Serializable {
    public static final int EVENT_MAX_PAYLOAD_SIZE = 64 * 1024;

    private final int eventLength;
    private EventState eventState;
    /**
     * The time stamp shall contain the number of milliseconds since 01.01.1970
     * 00:00:00 UTC.
     */
    private final long timeStamp;
    private final ArrayList<Long> taskID;
    private final int jobID;
    private final Target target;
    private final long timeToLive;
    private final int transferPriority;
    private final IdentifierType identifierType;
    /** dynamic length, payload max size is EVENT_MAX_PAYLOAD_SIZE */
    private final ArrayList<Integer> payload;

    public Event(final long timeStamp, final long[] taskID, final int jobID, final int target, final long timeToLive,
            final int transferPriority, final int identifierType, final int[] payload) {

        if (validateEvent(target, payload.length)) {
            this.eventLength = taskID.length + payload.length;

            this.eventState = eventState_InReceiving;

            this.taskID = new ArrayList<Long>();
            for (long i : taskID)
                this.taskID.add(i);

            this.payload = new ArrayList<Integer>();
            for (int i : payload)
                this.payload.add(i);

            this.timeStamp = timeStamp;
            this.jobID = jobID;
            this.target = Target.getInstance(target);
            /* this.target = Target.values()[target]; */
            this.timeToLive = timeToLive;
            this.transferPriority = transferPriority;
            this.identifierType = IdentifierType.values()[identifierType];
        } else {
            this.eventState = eventState_Undefine;
            this.eventLength = 0;
            this.taskID = null;
            this.payload = null;
            this.timeStamp = 0;
            this.jobID = 0;
            this.target = Target.Unknown;
            this.timeToLive = 0;
            this.transferPriority = 0;
            this.identifierType = IdentifierType.IdentifierType_No_Identifier;
        }
    }

    public int getEventLength() {
        return eventLength;
    }

    public EventState getEventState() {
        return eventState;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public ArrayList<Long> getTaskID() {
        return taskID;
    }

    public int getJobID() {
        return jobID;
    }

    public Target getTarget() {
        return target;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public int getTransferPriority() {
        return transferPriority;
    }

    public IdentifierType getIdentifierType() {
        return identifierType;
    }

    public ArrayList<Integer> getPayload() {
        return payload;
    }

    public synchronized void setEventState(final EventState eventState) {
        this.eventState = eventState;
    }

    private boolean validateEvent(final int target, final int payload_size) {
        if ((Backend.equal(target)) || (Backend_Small.equal(target)) || (Backend_Large.equal(target))
                || (Backend_RTM.equal(target))) {
            if (payload_size <= EVENT_MAX_PAYLOAD_SIZE) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public long getEventExistTime() {
        return (timeStamp + timeToLive);
    }

    @Override
    public synchronized int compareTo(Event o) {
        if (this.getTransferPriority() == o.getTransferPriority())
            /** The same priority here, the longest remaining is head */
            return this.getEventExistTime() <= o.getEventExistTime() ? 1 : -1;
        else
            /** The higher priority is head */
            return this.getTransferPriority() < o.getTransferPriority() ? 1 : -1;
    }
}
