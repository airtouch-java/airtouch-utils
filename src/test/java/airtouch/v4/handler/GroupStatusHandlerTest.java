package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.Request;
import airtouch.ResponseList;
import airtouch.utils.HexString;
import airtouch.v4.constant.MessageConstants;
import airtouch.model.ZoneStatusResponse;

public class GroupStatusHandlerTest {

    @Test
    public void testGeneratingGroupStatusRequest() {
        Request<MessageConstants.Address> request = GroupStatusHandler.generateRequest(1, 0);
        assertEquals("555580b0012b0000f52f".toUpperCase(), request.getHexString());
    }

    @Test
    public void testHandleGroupStatusResponse() {
        // This data is copied from AirTouch4 protocol doc page 8.
        // 5555 b080 01 2b 000c 40640000ff0041e41a806180 6579
        //                      ^----- data block -----^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "40640000ff0041e41a806180";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<ZoneStatusResponse> response = GroupStatusHandler.handle(0, dataBlockBytes);
        assertEquals(2, response.size());
    }

    @Test
    public void testHandleReadlGroupStatusResponse() {
        // hexresponse=5555B0803C2B0018408A178A5B27018A17805CC7428018805C47039417805E47B108
        // [messageId=60, type=GROUP_STATUS, dataLength=24, hexData=408A178A5B27018A17805CC7428018805C47039417805E47]
        String dataBlockHexString = "408A178A5B27018A17805CC7428018805C47039417805E47";
        byte[] dataBlockBytes = HexString.toByteArray(dataBlockHexString);

        ResponseList<ZoneStatusResponse> response = GroupStatusHandler.handle(0, dataBlockBytes);
        assertEquals(4, response.size());

    }

}
