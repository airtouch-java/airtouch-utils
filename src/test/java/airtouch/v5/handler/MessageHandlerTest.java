package airtouch.v5.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.MessageType;
import airtouch.Response;
import airtouch.utils.HexString;

public class MessageHandlerTest {

    @Test
    public void test() {
        // This data is copied from AirTouch5 protocol doc page 9 (labeled 6).
        // 555555AA B080 01 C0 0018 21000000000800014080968002E700000164FF0007FF0000 491F
        //                          ^-----            data block              -----^
        String dataBlockHexString = "555555AAB08001C0001821000000000800014080968002E700000164FF0007FF0000491F";
        byte[] messsageBytes = HexString.toByteArray(dataBlockHexString);
        MessageHandler messageHandler = new MessageHandler();
        Response response = messageHandler.handle(messsageBytes);
        assertEquals(MessageType.ZONE_STATUS, response.getMessageType());
    }

}
