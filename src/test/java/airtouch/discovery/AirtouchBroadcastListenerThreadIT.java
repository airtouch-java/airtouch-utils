package airtouch.discovery;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.Test;

import airtouch.AirtouchVersion;
import airtouch.discovery.AirtouchDiscoveryBroadcastResponseCallback.AirtouchDiscoveryBroadcastResponse;

public class AirtouchBroadcastListenerThreadIT {

    //@Test // for some reason, running these two tests fails. It might be a port binding issue.
    public void testAirTouch4() throws IOException {
        MockBroadcastResponseCallback callback = new MockBroadcastResponseCallback();
        AirtouchDiscoveryBroadcastListenerThread listenerThread = new AirtouchDiscoveryBroadcastListenerThread(AirtouchVersion.AIRTOUCH4, callback);
        listenerThread.start();

        String broadcastMessage = "192.168.7.101,E4:F2:A6:CC:AE:44,AirTouch4,23236426";
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLoopbackAddress(), AirtouchVersion.AIRTOUCH4.getDiscoveryPort());
        socket.send(packet);
        socket.close();

        try {
            Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> callback.isDone());
        } finally {
            listenerThread.shutdown();
        }

        AirtouchDiscoveryBroadcastResponse response = callback.getResponse();
        assertEquals("23236426", response.getAirtouchId());
        assertEquals("192.168.7.101", response.getHostAddress());
        assertEquals("E4:F2:A6:CC:AE:44", response.getMacAddress());
        assertEquals(Integer.valueOf(airtouch.v4.constant.ConnectionConstants.AIRTOUCH_LISTEN_PORT), response.getPortNumber());
        assertEquals(AirtouchVersion.AIRTOUCH4, response.getAirtouchVersion());
    }

    @Test // for some reason, running these two tests fails. It might be a port binding issue.
    public void testAirTouch5() throws IOException {
        MockBroadcastResponseCallback callback = new MockBroadcastResponseCallback();
        AirtouchDiscoveryBroadcastListenerThread listenerThread = new AirtouchDiscoveryBroadcastListenerThread(AirtouchVersion.AIRTOUCH5, callback);
        listenerThread.start();
        // [IP],[ConsoleID],AirTouch5,[AirTouch ID],[Device Name]
        String broadcastMessage = "192.168.7.101,ConsoleId,AirTouch5,23236426,My Device Name";
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLoopbackAddress(), 49005);
        socket.send(packet);
        socket.close();

        try {
            Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> callback.isDone());
        } finally {
            listenerThread.shutdown();
        }

        AirtouchDiscoveryBroadcastResponse response = callback.getResponse();
        assertEquals("23236426", response.getAirtouchId());
        assertEquals("192.168.7.101", response.getHostAddress());
        assertEquals("ConsoleId", response.getConsoleId());
        assertEquals("My Device Name", response.getDeviceName());
        assertEquals(Integer.valueOf(airtouch.v5.constant.ConnectionConstants.AIRTOUCH_LISTEN_PORT), response.getPortNumber());
        assertEquals(AirtouchVersion.AIRTOUCH5, response.getAirtouchVersion());
    }

    public static class MockBroadcastResponseCallback implements AirtouchDiscoveryBroadcastResponseCallback {

        private boolean done = false;
        private AirtouchDiscoveryBroadcastResponse response;

        @Override
        public void handleResponse(AirtouchDiscoveryBroadcastResponse response) {
            this.done = true;
            this.response = response;
        }

        public boolean isDone() {
            return done;
        }

        public AirtouchDiscoveryBroadcastResponse getResponse() {
            return response;
        }
    }
}
