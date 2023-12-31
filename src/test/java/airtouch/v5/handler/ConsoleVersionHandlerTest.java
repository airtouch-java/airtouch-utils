package airtouch.v5.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.model.ConsoleVersionResponse;
import airtouch.utils.HexString;
import airtouch.v5.constant.MessageConstants;

public class ConsoleVersionHandlerTest {

    @Test
    public void testGeneratingConsoleVersionRequestForGroupZero() {
        Request<MessageConstants.Address> request = ConsoleVersionHandler.generateRequest(1);
        assertEquals("555555AA90b0011f0002ff309b8c".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleConsoleVersionResponse() {

        // This data is copied from AirTouch5 protocol doc page 18 (labeled as 15).
        // 555555AA b090 01 1f 001a ff30 000b312e302e332c312e302e33 2c0e
        //                               ^------ data block ------^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "000b312e302e332c312e302e33";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<ConsoleVersionResponse> response = ConsoleVersionHandler.handle(0, dataBlockBytes);
        System.out.println("##" + response + "##");

        assertEquals(1, response.size());
        ConsoleVersionResponse acConsoleVersion01 = response.get(0);
        assertFalse(acConsoleVersion01.isUpdateRequired());
        assertEquals("1.0.3", acConsoleVersion01.getVersions().get(0));
        assertEquals("1.0.3", acConsoleVersion01.getVersions().get(1));

    }

}
