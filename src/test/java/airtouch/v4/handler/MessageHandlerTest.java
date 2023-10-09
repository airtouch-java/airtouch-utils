package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.Response;
import airtouch.exception.UnknownAirtouchResponseException;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.utils.HexString;

public class MessageHandlerTest {

    @Test
    public void test() {
        // This data is copied from AirTouch4 protocol doc page 8.
        // 5555 b080 01 2b 000c 40640000ff0041e41a806180 6579
        //                      ^----- data block -----^
        String dataBlockHexString = "5555b080012b000c40640000ff0041e41a8061806579";
        byte[] messsageBytes = HexString.toByteArray(dataBlockHexString);
        MessageHandler messageHandler = new MessageHandler();
        Response response = messageHandler.handle(messsageBytes);
        assertEquals(MessageType.GROUP_STATUS.toString(), response.getMessageType());
    }

    @Test(expected=UnknownAirtouchResponseException.class)
    public void testStrangeMessage() {
        //
        String dataBlockHexString = "5555B080073700208000800080008000000000008000800000000000800080000000000080008000A7A9";
        byte[] messsageBytes = HexString.toByteArray(dataBlockHexString);
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.handle(messsageBytes);
    }

    @Test(expected=UnknownAirtouchResponseException.class)
    public void testStrangeMessage2() {
        //
        String dataBlockHexString = "5555B080002F01C403FF64842126100010160000048100000000000000000000000000000000000000000000000000000000"
                + "506F6C796169726500000000000000003201043900040D0080881F1D1E10554E495431000000000"
                + "4000080881F1D2010554E495432000000000"
                + "4000080881F1D2010554E495433000000000"
                + "4000080881F1D2010554E4954340000004D617374657200004C657669000000004769726C730000004A65766F6E00000047726F757035000047726F757036000047726F757037000047726F757038000047726F757039000047726F757041000047726F757042000047726F757043000047726F757044000047726F757045000047726F757046000047726F7570470000FFFFFFFF80004769726C730000000400464156320000000000004641563300000000000046415634000000000000800080000000000000000000000000004010110053C0000001004000FFE7FFFE02004000FFE7FFFE03004000FFE7FFFE4080118053E7418A118A53C7429911995387438011805407446419003E87456419003E87466419003E87476419003E87486419003E87496419003E874A6419003E874B6419003E874C6419003E874D6419003E874E6419003E874F6419003E870FA4";
        byte[] messsageBytes = HexString.toByteArray(dataBlockHexString);
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.handle(messsageBytes);
    }


}
