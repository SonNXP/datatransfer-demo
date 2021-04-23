package datatransfer.services;

import datatransfer.external.os.Handler;
import datatransfer.external.os.IBinder;
import datatransfer.external.os.IInterface;
import datatransfer.external.os.Message;

import datatransfer.mediator.Colleague;
import javax.annotation.Nonnull;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public abstract class Service implements Colleague {
    private static final Logger logger = LoggerFactory.getLogger(Service.class);

    protected static final int HANDLER_MESSAGE = 1;
    protected static final int SERVICE_RETRY_TIME_MS = 500;

    private IInterface mService;
    protected ServiceHandler mHandler;
    protected DeathRecipientImpl mDeathRecipient;

    public Service() {
        mHandler = new ServiceHandler();
        mDeathRecipient = new DeathRecipientImpl();
    }

    public abstract void initService();
    public abstract void registerReceiver(IInterface receiver);
    public abstract void unregisterReceiver(IInterface receiver);

    protected void setService(IInterface service) {
        mService = service;
    }

    /** Send a message that register the service again after 50mS when a service died */
    protected class DeathRecipientImpl implements IBinder.DeathRecipient {
        @Override
        public void binderDied(IBinder who) {
            IBinder bn = mService.asBinder();
            if(bn == who) {
                Message msg = mHandler.obtainMessage(HANDLER_MESSAGE);
                mHandler.sendMessageDelayed(msg, SERVICE_RETRY_TIME_MS);
                logger.debug("onBinderDied: " + mService + " retry in 500mS");
            }
        }
    }

    protected class ServiceHandler extends Handler {
        @Override
        public void handleMessage(@Nonnull Message msg) {
            if (msg.what == HANDLER_MESSAGE) {
                initService();
            }
        }
    }

}
