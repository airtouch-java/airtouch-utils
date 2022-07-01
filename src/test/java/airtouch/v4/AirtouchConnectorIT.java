package airtouch.v4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.junit.Test;

import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.handler.AcStatusHandler;
import airtouch.v4.handler.GroupNameHandler;
import airtouch.v4.handler.GroupStatusHandler;

@SuppressWarnings("rawtypes")
public class AirtouchConnectorIT {

    Map<Integer, Response> responses = new HashMap<>();
    
    @Test
    public void test() throws IOException {
        
        AtomicInteger counter = new AtomicInteger(0);
        
        String hostName = System.getenv("AIRTOUCH_HOST");
        int portNumber = 9004;
        
        AirtouchConnector airtouchConnector = new AirtouchConnector(new ResponseCallback() {
            @Override
            public void handleResponse(Response response) {
                responses.put(response.getMessageId(), response);
                System.out.println(response);
                counter.getAndIncrement();
            }
        });
        
        airtouchConnector.connect(hostName, portNumber);
        
        Thread t = new Thread(airtouchConnector);
        t.start();
        
        airtouchConnector.sendRequest(GroupStatusHandler.generateRequest(1, null));
        airtouchConnector.sendRequest(GroupNameHandler.generateRequest(2, null));
        airtouchConnector.sendRequest(AcStatusHandler.generateRequest(3, null));
        
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(counter, Matchers.equalTo(3));
        airtouchConnector.disconnect();
        
        assertTrue(responses.containsKey(1));
        assertEquals(MessageType.GROUP_STATUS, responses.get(1).getMessageType());
        
        assertTrue(responses.containsKey(2));
        assertEquals(MessageType.GROUP_NAME, responses.get(2).getMessageType());
        
        assertTrue(responses.containsKey(3));
        assertEquals(MessageType.AC_STATUS, responses.get(3).getMessageType());
    }

}
