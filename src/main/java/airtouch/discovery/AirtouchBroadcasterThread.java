package airtouch.discovery;

import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.AirtouchVersion;

public class AirtouchBroadcasterThread extends Thread implements Runnable {
    
    private static final String DEFAULT_THREAD_NAME = AirtouchBroadcasterThread.class.getSimpleName();

    private final Logger log = LoggerFactory.getLogger(AirtouchBroadcasterThread.class);

    private boolean stopping;

    private AirtouchVersion airtouchVersion;


    public AirtouchBroadcasterThread(AirtouchVersion airtouchVersion) {
        super(DEFAULT_THREAD_NAME);
        this.airtouchVersion = airtouchVersion;
    }
    
    public AirtouchBroadcasterThread(AirtouchVersion airtouchVersion, String threadName) {
        super(threadName);
        this.airtouchVersion = airtouchVersion;
    }
    
    public void shutdown() {
        this.stopping = true;
    }
    
    public boolean isRunning() {
        return !this.stopping;
    }
    
    @Override
    public void run() {
        while (!stopping) {
            try {
                for (int i = 0; i < 5; i++) {
                    BroadcastSender.broadcastAll(airtouchVersion);
                }
                sleep(1000L * 30);
            } catch (InterruptedException e) {
                this.interrupt();
            } catch (SocketException e) {
                log.warn("Failed to send UDP broadcast for Airtouch Discovery. {}", e.getMessage());
            }
        }
    }
}