import datatransfer.event.Event;
import datatransfer.executors.AppExecutor;
import datatransfer.external.RtmManager;
import datatransfer.mediator.AppMediator;
import datatransfer.queue.inputqueue.InputQueue;
import datatransfer.queue.outputqueue.OutputQueue;
import datatransfer.services.RtmService;
import datatransfer.services.SomeIPConsumer;
import datatransfer.transmission.RtmTransmission;

public class App {
    static final int EVENT_NUMBER_MAX = 10;

    public static void makeEvents(SomeIPConsumer service) {
        long timeStamp = 1000000;
        int jobID = 5;
        int target = 7;
        long timeToLive = 2000000;
        int transferPriority = 100;
        int identifierType = 3;

        long[] taskID = new long[10];
        for (int i = 0; i < taskID.length; i++)
            taskID[i] = 10L;

        int[] payload = new int[1000];
        for (int i = 0; i < payload.length; i++)
            payload[i] = 1000;

        for (int i = 0; i < EVENT_NUMBER_MAX; i++) {
            service.setEventChange(timeStamp, taskID, jobID, target, timeToLive, transferPriority, identifierType,
                    payload);
            if (i >= 5) {
                if (i >= 7)
                    transferPriority--;
                timeToLive++;
            }
            if (i >= 3)
                transferPriority++;
        }
    }

    public static void main(String[] args) {
        /**
         * ! For better performance, should design a fine grained blocking/priority !
         * queue to use for the input queue/output queue. That can run add and take at
         * almost the same time.
         */
        final AppExecutor appExecutor = new AppExecutor();
        final InputQueue inputQueue = new InputQueue(appExecutor);
        final OutputQueue outputQueue = new OutputQueue(appExecutor);

        final RtmService rtmService = new RtmService();
        final RtmManager rtmManager = new RtmManager();
        final RtmTransmission rtmTransmission = new RtmTransmission(rtmService, rtmManager);
        final AppMediator appMediator = new AppMediator(inputQueue, outputQueue, rtmTransmission, appExecutor);

        SomeIPConsumer someIPConsumer = new SomeIPConsumer();
        someIPConsumer.setMediator(appMediator);

        /** For demo only */
        makeEvents(someIPConsumer);

        while (!outputQueue.isEmpty()) {
            Event event = null;
            try {
                event = outputQueue.take();
            } catch (InterruptedException e) {

            } finally {
                System.out.println(event.getTransferPriority() + " - " + event.getEventExistTime());
            }
        }

        System.out.println("Finish the demo!");
    }
}
