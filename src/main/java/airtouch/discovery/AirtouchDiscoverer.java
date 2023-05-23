package airtouch.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.AirtouchVersion;

public class AirtouchDiscoverer {

    private final Logger log = LoggerFactory.getLogger(AirtouchDiscoverer.class);
    private final AirtouchVersion airtouchVersion;
    private final AirtouchDiscoveryBroadcastResponseCallback responseCallback;

    private AirtouchDiscoveryBroadcasterThread broadcasterThread;
    private AirtouchDiscoveryBroadcastListenerThread listenerThread;

    public AirtouchDiscoverer(final AirtouchVersion airtouchVersion, final AirtouchDiscoveryBroadcastResponseCallback responseCallback) {
        this.airtouchVersion = airtouchVersion;
        this.responseCallback = responseCallback;
    }

    public void start() {
        this.listenerThread = new AirtouchDiscoveryBroadcastListenerThread(this.airtouchVersion, this.responseCallback);
        this.listenerThread.start();
        this.broadcasterThread = new AirtouchDiscoveryBroadcasterThread(airtouchVersion);
        this.broadcasterThread.start();
    }
    public void shutdown() {
        log.debug("Shutdown called");
        if (this.isRunning()) {
            this.broadcasterThread.shutdown();
            this.listenerThread.shutdown();
            log.info("AirtouchBroadcaster shutdown completed");
        }
    }
    public boolean isRunning() {
        return this.broadcasterThread != null && this.broadcasterThread.isRunning()
               || this.listenerThread != null && this.listenerThread.isRunning();
    }
}
