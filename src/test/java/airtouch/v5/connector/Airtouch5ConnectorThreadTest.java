package airtouch.v5.connector;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.Test;

import airtouch.Response;
import airtouch.ResponseCallback;
import airtouch.connector.AirtouchConnectorThread;
import airtouch.constant.AirConditionerStatusConstants.FanSpeed;
import airtouch.constant.AirConditionerStatusConstants.Mode;
import airtouch.constant.AirConditionerStatusConstants.PowerState;
import airtouch.model.AirConditionerStatusResponse;
import airtouch.utils.HexString;

public class Airtouch5ConnectorThreadTest implements ResponseCallback {
    
    boolean isDone = false;
    List<AirConditionerStatusResponse> responses;

    @Test
    public void testACstatusMessage() {
        //String response = "555555AAB08001C0001821000000000800014080968002E700000164FF0007FF0000491F";
        String response = "555555AAB08001C0001C23000000000A0002101278C002DA00008000014264C002E4000080003D79";
        InputStream input = new ByteArrayInputStream(HexString.toByteArray(response));
        AirtouchConnectorThread thread = new Airtouch5ConnectorThreadFactory().create(input, this);
        thread.start();
        
        try {
            Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> this.isDone());
        } finally {
            thread.shutdown();
        }
        
        AirConditionerStatusResponse r1 = responses.get(0);
        assertEquals("First AC has wrong index", new Integer(0), r1.getAcNumber());
        assertEquals( 22, (int)r1.getTargetSetpoint());
        assertEquals( "CurrentTemperature unexpected value.", new Double(23.0), r1.getCurrentTemperature());
        assertEquals( PowerState.ON, r1.getPowerstate());
        assertEquals( Mode.HEAT, r1.getMode());
        assertEquals( "FanSpeed unexpected value.", FanSpeed.LOW, r1.getFanSpeed());
        assertEquals( 0, r1.getErrorCode());
        
        AirConditionerStatusResponse r2= responses.get(1);
        assertEquals("Second AC has wrong index", new Integer(1), r2.getAcNumber());
        assertEquals( 20, (int)r2.getTargetSetpoint());
        assertEquals( new Double(24.0), r2.getCurrentTemperature());
        assertEquals( PowerState.OFF, r2.getPowerstate());
        assertEquals( Mode.COOL, r2.getMode());
        assertEquals( FanSpeed.LOW, r2.getFanSpeed());
        assertEquals( 0, r2.getErrorCode());

    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleResponse(Response response) {
        responses = response.getData();
        this.isDone  = true;
    }
    
    private boolean isDone() {
        return this.isDone;
    }

}

//FF13000A4D61737465720000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
