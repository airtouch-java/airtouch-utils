package airtouch.v5.handler;

import org.junit.Test;

import airtouch.utils.HexString;

public class AbstractHandlerTest {

    @Test(expected=IllegalArgumentException.class)
    public void testCheckHeaderIsRemovedThrowsExceptionWhenDataBlockStartsWithHeader() {
        
        // This data is copied from AirTouch5 protocol doc page 8 (labeled as page 6).
        //
        String responseHexString = "555555AAB08001C0001821000000000800014080968002E700000164FF0007FF0000491F";
        byte[] responseBytes = HexString.toByteArray(responseHexString);
        AbstractHandler.checkHeaderIsRemoved(responseBytes);
    }

}
