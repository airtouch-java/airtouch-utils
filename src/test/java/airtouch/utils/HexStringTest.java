package airtouch.utils;

import org.junit.Assert;
import org.junit.Test;

public class HexStringTest {

    @Test
    public void test() {
        String[] tests = {"0FA1056D73", "", "00", "0123456789ABCDEF", "FFFFFFFF"};

        for (int i = 0; i < tests.length; i++) {
            String in = tests[i];
            byte[] bytes = HexString.toByteArray(in);
            String out = HexString.fromBytes(bytes);
            System.out.println(in); //DEBUG
            System.out.println(out); //DEBUG
            Assert.assertEquals(in, out);

        }

    }

}
