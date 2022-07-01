package airtouch.v4.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.v4.Request;
import airtouch.v4.constant.AcControlConstants.AcPower;
import airtouch.v4.constant.AcControlConstants.Mode;
import airtouch.v4.model.AcControlRequest;

public class AcControlHandlerTest {

    @Test
    public void testGenerateRequestPowerOffSecondAc() {
        AcControlRequest acControlRequest = new AcControlHandler.RequestBuilder(1)
                .acPower(AcPower.POWER_OFF)
                .build();
        
        Request request = AcControlHandler.generateRequest(1, acControlRequest);
        assertEquals("555580b0012c000481ff0000ea87".toUpperCase(), request.getHexString());
    }
    
    @Test
    public void testGenerateRequestSetFirstAcToCoolMode() {
        AcControlRequest acControlRequest = new AcControlHandler.RequestBuilder(0)
                .acMode(Mode.COOL)
                .build();
        
        Request request = AcControlHandler.generateRequest(1, acControlRequest);
        assertEquals("555580b0012c000400400000329e".toUpperCase(), request.getHexString());
    }

}
