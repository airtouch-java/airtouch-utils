package airtouch.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.AirtouchVersion;

public class AirtouchDiscoveryBroadcastSender {
    private AirtouchDiscoveryBroadcastSender() {} // Prevent instantiation.
    private static final Logger log = LoggerFactory.getLogger(AirtouchDiscoveryBroadcastSender.class);


    private static DatagramSocket socket = null;
    
    public static void broadcastAll(AirtouchVersion airtouchVersion) throws SocketException {
        List<InetAddress> addresses = listAllBroadcastAddresses();
        AtomicBoolean success = new AtomicBoolean(false);
        Map<InetAddress, IOException> exceptions = new HashMap<>();
        addresses.forEach(i -> {
            try {
                broadcast(airtouchVersion.getDiscoveryMessage(), i, airtouchVersion.getDiscoveryPort());
                success.set(true);
            } catch (IOException e) {
                exceptions.put(i,e);
            }
        });
        if (!success.get()) {
            exceptions.forEach((i,e) -> {
                log.warn("Failed to send discovery message to '{}:{}'. {}", 
                        i.getHostAddress(), 
                        airtouchVersion.getDiscoveryPort(),
                        e.getMessage());
            });
        }
    }

    public static void broadcast(String broadcastMessage, InetAddress address, int port) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
        socket.close();
    }
    
    public static List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces 
          = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream() 
              .map(a -> a.getBroadcast())
              .filter(Objects::nonNull)
              .forEach(broadcastList::add);
        }
        return broadcastList;
    }
}