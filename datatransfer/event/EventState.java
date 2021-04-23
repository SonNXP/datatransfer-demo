package datatransfer.event;

public enum EventState {
    eventState_InReceiving(0),
    eventState_InQueue(1),
    eventState_InStorage(2),
    eventState_InSendlist(3),
    eventState_ToClassifying(4),
    eventState_isChecked(5),
    eventState_IsSent(6),
    eventState_InScanning(7),
    eventState_ForwardRTM(8),
    eventState_Undefine(255);

    private int eventState;

    EventState(int eventState) {
        this.eventState = eventState;
    }
}
