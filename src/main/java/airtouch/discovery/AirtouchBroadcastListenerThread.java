package airtouch.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.AirtouchVersion;
import airtouch.discovery.BroadcastResponseCallback.BroadcastResponse;

public class AirtouchBroadcastListenerThread extends Thread implements Runnable {

    private static final String DEFAULT_THREAD_NAME = AirtouchBroadcastListenerThread.class.getSimpleName();

    private final Logger log = LoggerFactory.getLogger(AirtouchBroadcastListenerThread.class);

    private boolean stopping;
    private final AirtouchVersion airtouchVersion;
    private final BroadcastResponseCallback responseCallback;


    public AirtouchBroadcastListenerThread(final AirtouchVersion airtouchVersion, final BroadcastResponseCallback responseCallback) {
        super(DEFAULT_THREAD_NAME);
        this.airtouchVersion = airtouchVersion;
        this.responseCallback = responseCallback;
    }

    public AirtouchBroadcastListenerThread(final AirtouchVersion airtouchVersion, final BroadcastResponseCallback responseCallback, String threadName) {
        super(threadName);
        this.airtouchVersion = airtouchVersion;
        this.responseCallback = responseCallback;
    }

    public void shutdown() {
        this.stopping = true;
    }

    public boolean isRunning() {
        return !this.stopping;
    }

    @Override
    public void run() {
        try(DatagramSocket socket = new DatagramSocket(this.airtouchVersion.getDiscoveryPort(), InetAddress.getByName("0.0.0.0"))) {
            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (!stopping) {
                socket.receive(packet);
                BroadcastResponse broadcastResponse = BroadcastResponseParser.parse(new String(packet.getData(), packet.getOffset(), packet.getLength()));
                if (broadcastResponse != null) {
                this.responseCallback.handleResponse(broadcastResponse);
                }
            }
        } catch (IOException e) {
            log.warn("Failed to start discovery listener. It will not be possible to auto-discover the Airtouch on the network. Reason: {}", e.getMessage(), e);
        }
    }
}