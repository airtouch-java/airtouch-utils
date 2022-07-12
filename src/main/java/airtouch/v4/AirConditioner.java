package airtouch.v4;

import airtouch.v4.builder.AirConditionerControlRequestBuilder;
import airtouch.v4.handler.AirConditionerControlHandler;

public class AirConditioner {

    public static AirConditionerControlRequestBuilder acNumber(int acNumber) {
        return new AirConditionerControlRequestBuilder().acNumber(acNumber);
    }

    public static Request buildRequest(int messageId, AirConditionerControlRequestBuilder requestBuilder) {
        return AirConditionerControlHandler.generateRequest(messageId, requestBuilder.build());
    }
}
