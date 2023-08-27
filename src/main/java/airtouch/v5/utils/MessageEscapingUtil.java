package airtouch.v5.utils;

import java.nio.ByteBuffer;

public class MessageEscapingUtil {
    
    private static byte x55 = (byte) 0x55;
    private static byte x00 = (byte) 0x00;
    
    /**
     * Searches the airtouchMessage for a set of bytes that looks like "55 55 55"
     * and adds the "00" from the end.<br>
     * 
     * As per Airtouch5 docs page 6 (labeled as 3), certain messages that look like headers
     * are escaped so that they don't get mistaken for a header. See note below copied from the docs.<p>
     * 
     * <b>Note: Expects to handle the whole message from the Airtouch. The first 4 bytes
     * are the header and are not evaluated by this method.
     * The checksum is not expected to be valid after the extra bytes have been added.</b><br>
     * 
     * <code><pre>
     * Redundant bytes in message (copied from docs)
     * To prevent the message from containing the same data as header, a 00 is inserted after 
     * every three consecutive 0x55s in the message. The inserted 00 is redundant bytes.
     * </pre></code>
     * 
     * @param airtouchMessage - byte[] of the message destined for the Airtouch. Must include header.
     * @return a new byte[] array containing the input but with 555555 converted to 55555500 (except for the header)
     */
    public static byte[] addMessageEscaping(byte[] airtouchMessage) {
        ByteBuffer buffer = ByteBuffer.allocate(airtouchMessage.length * 2);
        for (int i = 0; i < airtouchMessage.length; i++) {
            
            // The first four bytes are header, so don't both checking until byte 7 (6 when 0 based).
            if (i >= 6) {
                if (airtouchMessage[i-2] == x55 
                 && airtouchMessage[i-1] == x55
                 && airtouchMessage[i] == x55) 
                {
                    buffer.put(airtouchMessage[i]);
                    // The current bytes are 0x55 0x55 0x55. 
                    // Therefore, add the 0x00 to the buffer.
                    buffer.put(x00);
                } else {
                    buffer.put(airtouchMessage[i]);
                }
            } else {
                buffer.put(airtouchMessage[i]);
            }
        }
        
        final byte[] bs = new byte[buffer.position()];
        final byte[] bufferArray = buffer.array();
        for (int i = 0; i < bs.length; i++) {
            bs[i] = bufferArray[i];
        }
        return bs;
    }
    
    /**
     * Searches the rawAirtouchMessage for a set of bytes that looks like "55 55 55 00"
     * and removes the "00" from the end.<br>
     * 
     * As per Airtouch5 docs page 6 (labeled as 3), certain messages that look like headers
     * are escaped so that they don't get mistaken for a header. See note below copied from the docs.<p>
     * 
     * <b>Note: Expects to handle the whole message from the Airtouch. The first 4 bytes
     * are the header and are not evaluated by this method.
     * The checksum is not expected to be valid until the redundant bytes have been removed.</b><br>
     * 
     * <code><pre>
     * Redundant bytes in message (copied from docs)
     * To prevent the message from containing the same data as header, a 00 is inserted after 
     * every three consecutive 0x55s in the message. The inserted 00 is redundant bytes.
     * </pre></code>
     * 
     * @param rawAirtouchMessage - byte[] of the message from the Airtouch. Must include header.
     * @return a new byte[] array containing the input but with 55555500 converted to 555555
     */
    public static byte[] removeMessageEscaping(byte[] rawAirtouchMessage) {
        ByteBuffer buffer = ByteBuffer.allocate(rawAirtouchMessage.length);
        for (int i = 0; i < rawAirtouchMessage.length; i++) {
            
            // The first four bytes are header, so don't both checking until byte 8 (7 when 0 based).
            if (i >= 7) {
                if (rawAirtouchMessage[i-3] == x55 
                 && rawAirtouchMessage[i-2] == x55
                 && rawAirtouchMessage[i-1] == x55 
                 && rawAirtouchMessage[i] == x00) 
                {
                    // The current bytes are 0x55 0x55 0x55 0x00. 
                    // Therefore, don't add the 0x00 to the buffer.
                } else {
                    buffer.put(rawAirtouchMessage[i]);
                }
            } else {
                buffer.put(rawAirtouchMessage[i]);
            }
        }
        
        final byte[] bs = new byte[buffer.position()];
        final byte[] bufferArray = buffer.array();
        for (int i = 0; i < bs.length; i++) {
            bs[i] = bufferArray[i];
        }
        return bs;
    }

}
