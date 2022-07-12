package airtouch.v4;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import airtouch.v4.constant.AirConditionerControlConstants.AcPower;

public class AirConditionerTest {

    @Test
    public void test() {
        Request request = AirConditioner.buildRequest(1,
                AirConditioner.acNumber(1)
                .acPower(AcPower.POWER_OFF)
                .acNumber(1)
            );
        assertEquals("555580b0012c000481ff3f001a96".toUpperCase(), request.getHexString());
    }

}
