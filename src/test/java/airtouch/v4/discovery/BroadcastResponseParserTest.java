package airtouch.v4.discovery;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.AirtouchVersion;
import airtouch.v4.constant.ConnectionConstants;
import airtouch.v4.discovery.BroadcastResponseCallback.BroadcastResponse;

public class BroadcastResponseParserTest {

    @Test
    public void testParse() {
        BroadcastResponse response = BroadcastResponseParser.parse("192.168.7.101,E4:F2:A6:CC:AE:44,AirTouch4,23236426");
        assertEquals("23236426", response.getAirtouchId());
        assertEquals("192.168.7.101", response.getHostAddress());
        assertEquals("E4:F2:A6:CC:AE:44", response.getMacAddress());
        assertEquals(Integer.valueOf(ConnectionConstants.AIRTOUCH_LISTEN_PORT), response.getPortNumber());
        assertEquals(AirtouchVersion.AirTouch4, response.getAirtouchVersion());
    }

}
