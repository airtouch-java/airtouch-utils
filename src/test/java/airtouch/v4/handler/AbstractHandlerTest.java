package airtouch.v4.handler;

import org.junit.Test;

import airtouch.exception.IllegalAirtouchResponseException;
import airtouch.v4.utils.HexString;

public class AbstractHandlerTest {

    @Test(expected=IllegalAirtouchResponseException.class)
    public void testCheckHeaderIsRemovedThrowsExceptionWhenDataBlockStartsWithHeader() {

        // This data is copied from AirTouch4 protocol doc page 8.
        String responseHexString = "5555b080012b000c40640000ff0041e41a8061806579";
        byte[] responseBytes = HexString.toByteArray(responseHexString);
        AbstractHandler.checkHeaderIsRemoved(responseBytes);
    }

}
