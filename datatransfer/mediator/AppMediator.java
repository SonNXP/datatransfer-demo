package datatransfer.mediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datatransfer.driveevent.DriveEvent;
import datatransfer.event.Event;
import datatransfer.executors.AppExecutor;
import datatransfer.queue.inputqueue.InputQueue;
import datatransfer.queue.outputqueue.OutputQueue;
import datatransfer.transmission.RtmTransmission;

public class AppMediator implements Mediator {
    private static final Logger logger = LoggerFactory.getLogger(AppMediator.class);

    private InputQueue inputQueue;
    private OutputQueue outputQueue;
    private RtmTransmission rtmTransmission;
    private AppExecutor appExecutor;
    private DriveEvent driveEvent;

    public AppMediator(InputQueue inputQueue, OutputQueue outputQueue, RtmTransmission rtmTransmission,
            AppExecutor appExecutor) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.rtmTransmission = rtmTransmission;
        this.appExecutor = appExecutor;
        this.driveEvent = new DriveEvent(inputQueue, this);
    }

    @Override
    public void receiveEvent(Event event) {
        /** Start a ReceiveEventExecutor thread in add() method below */
        inputQueue.add(event);
        driveEvent();
    }

    @Override
    public void driveEvent() {
        /** Run driveEvent by mainthread */
        driveEvent.run();
    }

    @Override
    public void forwardEventToRTM(Event event) {
        /** Run forwardEventToRTM by mainthread */
        rtmTransmission.buildSendingQueue(event);
    }

    @Override
    public void storeEvent(Event event) {
        /** Run storeEvent by mainthread */
        outputQueue.add(event);
    }

    @Override
    public void sendEventToBackend() {
        /** Run sendEventToBackend by ScanIntervalExecutor */
        // TODO add send to backend 
    }

    @Override
    public void sendStatus() {
        /** Run sendStatus by SendReceiverStatusExecutor */
        // TODO add send status
    }

    @Override
    public void updateInterfacesStatus() {
        /** Run updateInterfacesStatus by ReceiveInterfaceStateExecutor */
        // TODO add update interface status
    }
}
