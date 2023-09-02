package airtouch.discovery;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.AirtouchVersion;
import airtouch.discovery.AirtouchDiscoveryBroadcastResponseCallback.AirtouchDiscoveryBroadcastResponse;

public class BroadcastResponseParserTest {

    @Test
    public void testParseAirTouch4() {
        AirtouchDiscoveryBroadcastResponse response = AirtouchDiscoveryBroadcastResponseParser.parse("192.168.7.101,E4:F2:A6:CC:AE:44,AirTouch4,23236426");
        assertEquals("23236426", response.getAirtouchId());
        assertEquals("192.168.7.101", response.getHostAddress());
        assertEquals("E4:F2:A6:CC:AE:44", response.getMacAddress());
        assertEquals(Integer.valueOf(airtouch.v4.constant.ConnectionConstants.AIRTOUCH_LISTEN_PORT), response.getPortNumber());
        assertEquals(AirtouchVersion.AIRTOUCH4, response.getAirtouchVersion());
    }

    @Test
    public void testParseAirTouch5() {
        AirtouchDiscoveryBroadcastResponse response = AirtouchDiscoveryBroadcastResponseParser.parse("192.168.7.101,ConsoleId,AirTouch5,23236426,My Device Name");
        assertEquals("23236426", response.getAirtouchId());
        assertEquals("192.168.7.101", response.getHostAddress());
        assertEquals("ConsoleId", response.getConsoleId());
        assertEquals(Integer.valueOf(airtouch.v5.constant.ConnectionConstants.AIRTOUCH_LISTEN_PORT), response.getPortNumber());
        assertEquals(AirtouchVersion.AIRTOUCH5, response.getAirtouchVersion());
    }

}
