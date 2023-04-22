package airtouch.v4.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirtouchBroadcaster {
    
    private final Logger log = LoggerFactory.getLogger(AirtouchBroadcaster.class);
    private final BroadcastResponseCallback responseCallback;
    
    private AirtouchBroadcasterThread broadcasterThread;
    private AirtouchBroadcastListenerThread listenerThread;

    public AirtouchBroadcaster(final BroadcastResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }
    
    public void start() {
        this.listenerThread = new AirtouchBroadcastListenerThread(responseCallback);
        this.listenerThread.start();
        this.broadcasterThread = new AirtouchBroadcasterThread();
        this.broadcasterThread.start();
    }
    public void shutdown() {
        log.debug("Shutdown called");
        this.broadcasterThread.shutdown();
        this.listenerThread.shutdown();
        log.info("AirtouchBroadcaster shutdown completed");
    }
    public boolean isRunning() {
        return this.broadcasterThread != null && this.broadcasterThread.isRunning()
               || this.listenerThread != null && this.listenerThread.isRunning();
    }
}
