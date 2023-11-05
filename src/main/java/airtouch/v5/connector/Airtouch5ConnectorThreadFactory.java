package airtouch.v5.connector;

import java.io.InputStream;

import airtouch.ResponseCallback;
import airtouch.connector.AirtouchConnectorThread;
import airtouch.connector.AirtouchConnectorThreadFactory;
import airtouch.v5.constant.MessageConstants;
import airtouch.v5.constant.MessageConstants.Address;

public class Airtouch5ConnectorThreadFactory implements AirtouchConnectorThreadFactory<MessageConstants.Address> {

    @Override
    public AirtouchConnectorThread<Address> create(InputStream input, ResponseCallback responseCallback) {
        return new Airtouch5ConnectorThread<MessageConstants.Address>(input, responseCallback);
    }

}
