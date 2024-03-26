package airtouch.v5.connector;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.Test;

import airtouch.Response;
import airtouch.ResponseCallback;
import airtouch.connector.AirtouchConnectorThread;
import airtouch.utils.HexString;

public class Airtouch5ConnectorThreadTest implements ResponseCallback {
    
    boolean isDone = false;

    @Test
    public void test() {
        //String response = "555555AAB08001C0001821000000000800014080968002E700000164FF0007FF0000491F";
        String response = "555555AAB08001C0001C23000000000A0002101278C002DA00008000014264C002E4000080003D79";
        InputStream input = new ByteArrayInputStream(HexString.toByteArray(response));
        AirtouchConnectorThread thread = new Airtouch5ConnectorThreadFactory().create(input, this);
        thread.start();
        
        try {
            Awaitility.await().atMost(500, TimeUnit.SECONDS).until(() -> this.isDone());
        } finally {
            thread.shutdown();
        }
    }

    @Override
    public void handleResponse(Response response) {
        this.isDone  = true;
    }
    
    private boolean isDone() {
        return this.isDone;
    }

}

//FF13000A4D61737465720000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
