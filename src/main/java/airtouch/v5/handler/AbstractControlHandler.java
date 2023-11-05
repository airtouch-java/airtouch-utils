package airtouch.v5.handler;

import java.nio.ByteBuffer;

import airtouch.model.ControlRequest;

public abstract class AbstractControlHandler extends AbstractHandler {
    
    protected static ByteBuffer assembleRequest(int messageSubType, ControlRequest... controlRequests) {
        int totalSize = (controlRequests.length * 4) + 8; 
        ByteBuffer byteBuffer = ByteBuffer.allocate(totalSize);
        
        byteBuffer.put((byte) messageSubType);
        byteBuffer.put((byte) 0x0); // Byte after a subMessageType is always '0x00'
        
        byteBuffer.put((byte) 0x0); // No "normal data", so these next 2 bytes are zeros. 
        byteBuffer.put((byte) 0x0);
        
        byteBuffer.put((byte) 0x0); // Our actual data is 4 bytes so add 0x0004
        byteBuffer.put((byte) 0x4);
        
        byteBuffer.put((byte) 0x0); 
        byteBuffer.put((byte) (controlRequests.length & 0xFF)); // Number of repeats of the data.
        
        for (int i = 0; i < controlRequests.length; i++) {
            byteBuffer.put(controlRequests[i].getBytes());
        }
        return byteBuffer;
    }

}
