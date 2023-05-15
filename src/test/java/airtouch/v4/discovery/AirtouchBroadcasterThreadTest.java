package airtouch.v4.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.Ignore;
import org.junit.Test;

public class AirtouchBroadcasterThreadTest {

    @Test @Ignore
    public void test() throws IOException, InterruptedException {
        MockBroadcastResponseCallback callback = new MockBroadcastResponseCallback();
        AirtouchBroadcasterThread broadcastThread = new AirtouchBroadcasterThread();

        /*Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> callback.isDone());
        
                try (DatagramSocket socket = new DatagramSocket(49004, InetAddress.getByName("0.0.0.0"))) {
                //try (DatagramSocket socket = new DatagramSocket(49004)) {
                //try (DatagramSocket socket = new DatagramSocket(49004, InetAddress.getByName("127.0.0.1"))) {

                    broadcastThread.start();

                    byte[] buf = new byte[512];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String thing = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    System.out.println(thing);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        
//            if (thing != null) {
//                callback.handleResponse(response);
//            }
//            
//            
//            assertEquals("23236426", response.getAirtouchId());
//            assertEquals("192.168.7.101", response.getHostAddress());
//            assertEquals("E4:F2:A6:CC:AE:44", response.getMacAddress());
//            assertEquals(Integer.valueOf(ConnectionConstants.AIRTOUCH_LISTEN_PORT), response.getPortNumber());
//            assertEquals(AirtouchVersion.AirTouch4, response.getAirtouchVersion());
        */

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
