package airtouch.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.AirtouchVersion;

public class AirtouchBroadcaster {
    
    private final Logger log = LoggerFactory.getLogger(AirtouchBroadcaster.class);
    private final AirtouchVersion airtouchVersion;
    private final BroadcastResponseCallback responseCallback;
    
    private AirtouchBroadcasterThread broadcasterThread;
    private AirtouchBroadcastListenerThread listenerThread;

    public AirtouchBroadcaster(final AirtouchVersion airtouchVersion, final BroadcastResponseCallback responseCallback) {
        this.airtouchVersion = airtouchVersion;
        this.responseCallback = responseCallback;
    }
    
    public void start() {
        this.listenerThread = new AirtouchBroadcastListenerThread(this.airtouchVersion, this.responseCallback);
        this.listenerThread.start();
        this.broadcasterThread = new AirtouchBroadcasterThread(airtouchVersion);
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
