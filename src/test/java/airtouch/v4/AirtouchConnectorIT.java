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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.v4.constant.AirConditionerControlConstants.AcPower;
import airtouch.v4.constant.GroupControlConstants.GroupPower;
import airtouch.v4.constant.MessageConstants.MessageType;
import airtouch.v4.handler.AirConditionerControlHandler;
import airtouch.v4.handler.AirConditionerStatusHandler;
import airtouch.v4.handler.GroupControlHandler;
import airtouch.v4.handler.GroupNameHandler;
import airtouch.v4.handler.GroupStatusHandler;

@SuppressWarnings("rawtypes")
public class AirtouchConnectorIT {

    private final Logger log = LoggerFactory.getLogger(AirtouchConnector.class);

    Map<Integer, Response> responses = new HashMap<>();

    @Test
    public void test() throws IOException {

        AtomicInteger counter = new AtomicInteger(0);

        String hostName = System.getenv("AIRTOUCH_HOST");
        int portNumber = 9004;

        AirtouchConnector airtouchConnector = new AirtouchConnector(hostName, portNumber, new ResponseCallback() {
            @Override
            public void handleResponse(Response response) {
                responses.put(response.getMessageId(), response);
                log.info(response.toString());
                counter.getAndIncrement();
            }
        });

        airtouchConnector.start();

        airtouchConnector.sendRequest(GroupStatusHandler.generateRequest(1, null));
        airtouchConnector.sendRequest(GroupNameHandler.generateRequest(2, null));
        airtouchConnector.sendRequest(AirConditionerStatusHandler.generateRequest(3, null));

        airtouchConnector.sendRequest(AirConditionerStatusHandler.generateRequest(4, null));
        airtouchConnector.sendRequest(AirConditionerControlHandler.generateRequest(5,
                new AirConditionerControlHandler.RequestBuilder().acNumber(0).acPower(AcPower.POWER_ON).build()));
        airtouchConnector.sendRequest(GroupControlHandler.generateRequest(6,
                new GroupControlHandler.RequestBuilder(0).power(GroupPower.POWER_ON).build()));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(counter, Matchers.greaterThanOrEqualTo(6));
        airtouchConnector.shutdown();

        assertTrue(responses.containsKey(1));
        assertEquals(MessageType.GROUP_STATUS, responses.get(1).getMessageType());

        assertTrue(responses.containsKey(2));
        assertEquals(MessageType.GROUP_NAME, responses.get(2).getMessageType());

        assertTrue(responses.containsKey(3));
        assertEquals(MessageType.AC_STATUS, responses.get(3).getMessageType());
    }

}
