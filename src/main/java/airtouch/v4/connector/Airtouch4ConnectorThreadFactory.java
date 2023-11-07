package airtouch.v4.connector;

import java.io.InputStream;

import airtouch.ResponseCallback;
import airtouch.connector.AirtouchConnectorThread;
import airtouch.connector.AirtouchConnectorThreadFactory;
import airtouch.v4.constant.MessageConstants;
import airtouch.v4.constant.MessageConstants.Address;

public class Airtouch4ConnectorThreadFactory implements AirtouchConnectorThreadFactory<MessageConstants.Address> {

    @Override
    public AirtouchConnectorThread<Address> create(InputStream input, ResponseCallback responseCallback) {
        return new Airtouch4ConnectorThread<>(input, responseCallback);
    }

}
