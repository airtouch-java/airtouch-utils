package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.model.ConsoleVersionResponse;
import airtouch.utils.HexString;

public class ConsoleVersionHandlerTest {

    @Test
    public void testGeneratingConsoleVersionRequestForGroupZero() {
        Request<MessageType> request = ConsoleVersionHandler.generateRequest(1);
        assertEquals("555590b0011f0002ff309b8c".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleConsoleVersionResponse() {
        // This data is copied from AirTouch4 protocol doc page 16.
        // 5555 b090 01 1f 001a ff30 000b312e332e337c312e332e33 2c0e
        //                           ^------ data block ------^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "000b312e332e337c312e332e33";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<ConsoleVersionResponse, MessageType> response = ConsoleVersionHandler.handle(0, dataBlockBytes);
        System.out.println("##" + response + "##");

        assertEquals(1, response.size());
        ConsoleVersionResponse acStatus01 = response.get(0);
        assertFalse(acStatus01.isUpdateRequired());
        assertEquals("1.3.3", acStatus01.getVersions().get(0));
        assertEquals("1.3.3", acStatus01.getVersions().get(1));

    }

}
