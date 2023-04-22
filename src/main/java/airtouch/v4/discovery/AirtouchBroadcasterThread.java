package airtouch.v4.discovery;

import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirtouchBroadcasterThread extends Thread implements Runnable {
    
    private static final String DEFAULT_THREAD_NAME = AirtouchBroadcasterThread.class.getSimpleName();

    private final Logger log = LoggerFactory.getLogger(AirtouchBroadcasterThread.class);

    private boolean stopping;


    public AirtouchBroadcasterThread() {
        super(DEFAULT_THREAD_NAME);
    }
    
    public AirtouchBroadcasterThread(String threadName) {
        super(threadName);
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
                    BroadcastSender.broadcastAll();
                }
                sleep(1000 * 30);
            } catch (InterruptedException e) {
                this.interrupt();
            } catch (SocketException e) {
                log.warn("Failed to send UDP broadcast for Airtouch Discovery. {}", e.getMessage());
            }
        }
    }
}