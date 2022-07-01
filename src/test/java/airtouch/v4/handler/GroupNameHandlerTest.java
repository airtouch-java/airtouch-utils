package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.v4.Request;

public class GroupNameHandlerTest {

    @Test
    public void testGeneratingGroupNameRequest() {
        Request request = GroupNameHandler.generateRequest(1, null);
        assertEquals("555590b0011f0002ff12820c".toUpperCase(), request.getHexString());
    }
    
    @Test
    public void testGeneratingGroupNameRequestForGroupZero() {
        Request request = GroupNameHandler.generateRequest(1, 0);
        assertEquals("555590b0011f0003ff1200f983".toUpperCase(), request.getHexString());
    }

}
