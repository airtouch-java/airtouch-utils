package airtouch.v5.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.MessageType;
import airtouch.Response;
import airtouch.exception.IllegalAirtouchResponseException;
import airtouch.model.AirConditionerAbilityResponse;
import airtouch.utils.HexString;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.model.SubMessageMetaData;

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

    
    @Test
    public void testHandleExtendedMessage() {
        // This data is copied from AirTouch5 protocol doc page 14
        // 555555AA B090 01 1F 001A FF11 0018554E49540000000000000000000000000004171D101f121f 855D
        //                               ^------------------- data block -------------------^
        
        String dataBlockHexString = "555555AAB090011F001CFF11001A554E49540000000000000000000000000004171D101f121f108D".toUpperCase();
        byte[] messsageBytes = HexString.toByteArray(dataBlockHexString);
        MessageHandler messageHandler = new MessageHandler();
        Response response = messageHandler.handle(messsageBytes);
        assertEquals(MessageType.AC_ABILITY, response.getMessageType());
        AirConditionerAbilityResponse r = (AirConditionerAbilityResponse) response.getData().get(0);
        assertEquals(4, r.getZoneCount());
        assertEquals(0, r.getStartGroupNumber());
        assertEquals("UNIT", r.getAcName());
        assertEquals(0, r.getAcNumber());
    }
    
    @Test(expected = IllegalAirtouchResponseException.class)
    public void testInvalidMessageHeader() {
        String dataBlockHexString = "555555AB0000004C004C";
        byte[] messsageBytes = HexString.toByteArray(dataBlockHexString);
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.handle(messsageBytes);
    }
    
    @Test
    public void testZoneStatus() {
    	String dataBlockHexString = "555555AAB08001C0005021000000000800094080788002CF00000180788002CB000002946E8002DB000003806E8002C7000004806E8002BF0000058A5A8002C1000006806E8002C900004780828002D200000880788002DA0000D207".toUpperCase();
        byte[] messsageBytes = HexString.toByteArray(dataBlockHexString);
        MessageHandler messageHandler = new MessageHandler();
        Response response = messageHandler.handle(messsageBytes);
        assertEquals(MessageType.ZONE_STATUS, response.getMessageType());
    }
    
    @Test
    public void testSubMessageAssembly() {
    	byte b0 = 0x20;
    	
    	byte[] airTouchMessage = { b0, 0x00, 0x00, 0x01, 0x00, 0x0a, 0x00, 0x08 };
		SubMessageMetaData subTypeMetaData = MessageHandler.determineSubMessageMetaData(airTouchMessage);
		assertEquals(MessageConstants.ControlOrStatusMessageSubType.ZONE_CONTROL, subTypeMetaData.getSubMessageType());
		assertEquals(1, subTypeMetaData.getNormalDataLength());
		assertEquals(10, subTypeMetaData.getEachRepeatDataLength());
		assertEquals(8, subTypeMetaData.getRepeatDataCount());
    }
}
