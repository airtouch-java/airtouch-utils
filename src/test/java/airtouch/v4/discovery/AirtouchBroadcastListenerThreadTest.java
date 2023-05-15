package airtouch.v4.discovery;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.Test;

import airtouch.AirtouchVersion;
import airtouch.v4.constant.ConnectionConstants;
import airtouch.v4.discovery.BroadcastResponseCallback.BroadcastResponse;

public class AirtouchBroadcastListenerThreadTest {

    @Test
    public void test() throws IOException {
        MockBroadcastResponseCallback callback = new MockBroadcastResponseCallback();
        AirtouchBroadcastListenerThread listenerThread = new AirtouchBroadcastListenerThread(callback);
        listenerThread.start();
        
        String broadcastMessage = "192.168.7.101,E4:F2:A6:CC:AE:44,AirTouch4,23236426";
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLoopbackAddress(), 49004);
        socket.send(packet);
        socket.close();
        
        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> callback.isDone());
        
        BroadcastResponse response = callback.getResponse();
        assertEquals("23236426", response.getAirtouchId());
        assertEquals("192.168.7.101", response.getHostAddress());
        assertEquals("E4:F2:A6:CC:AE:44", response.getMacAddress());
        assertEquals(Integer.valueOf(ConnectionConstants.AIRTOUCH_LISTEN_PORT), response.getPortNumber());
        assertEquals(AirtouchVersion.AirTouch4, response.getAirtouchVersion());
    }

    public static class MockBroadcastResponseCallback implements BroadcastResponseCallback {
        
        private boolean done = false;
        private BroadcastResponse response;

        @Override
        public void handleResponse(BroadcastResponse response) {
            this.done = true;
            this.response = response;
        }
        
        public boolean isDone() {
            return done;
        }
        
        public BroadcastResponse getResponse() {
            return response;
        }
    }
}
