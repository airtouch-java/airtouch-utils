package airtouch.v4.handler;

import static org.junit.Assert.*;

import org.junit.Test;

import airtouch.v4.Response;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.utils.HexString;

public class MessageHandlerTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void test() {
        // This data is copied from AirTouch4 protocol doc page 8.
        // 5555 b080 01 2b 000c 40640000ff0041e41a806180 6579
        //                      ^----- data block -----^
        // Just pass in the data block. The rest should have been
        // validated and removed earlier.
        String dataBlockHexString = "5555b080012b000c40640000ff0041e41a8061806579";
        byte[] messsageBytes = HexString.toByteArray(dataBlockHexString);
        MessageHandler messageHandler = new MessageHandler();
        Response response = messageHandler.handle(messsageBytes);
        assertEquals(MessageType.GROUP_STATUS, response.getMessageType());
    }

}
