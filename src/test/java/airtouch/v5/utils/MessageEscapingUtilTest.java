package airtouch.v5.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.utils.HexString;

public class MessageEscapingUtilTest {
    
    @Test
    public void testAddMessageEscaping() {
        byte[] escapedMessage = HexString.toByteArray("555555aab05555550006555555aa0102bb18");
        byte[] message = MessageEscapingUtil.addMessageEscaping(escapedMessage);
        System.out.println(HexString.fromBytes(message));
        assertEquals("555555aab055555500000655555500aa0102bb18".toUpperCase(), HexString.fromBytes(message));
    }

    @Test
    public void testRemoveMessageEscaping() {
        byte[] escapedMessage = HexString.toByteArray("555555aab055555500000655555500aa0102bb18");
        byte[] message = MessageEscapingUtil.removeMessageEscaping(escapedMessage);
        System.out.println(HexString.fromBytes(message));
        assertEquals("555555aab05555550006555555aa0102bb18".toUpperCase(), HexString.fromBytes(message));
    }

}
