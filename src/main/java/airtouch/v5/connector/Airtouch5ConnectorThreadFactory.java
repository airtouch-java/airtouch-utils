package airtouch.v5.connector;

import java.io.InputStream;

import airtouch.ResponseCallback;
import airtouch.connector.AirtouchConnectorThread;
import airtouch.connector.AirtouchConnectorThreadFactory;

public class Airtouch5ConnectorThreadFactory implements AirtouchConnectorThreadFactory {

    @Override
    public AirtouchConnectorThread create(InputStream input, ResponseCallback responseCallback) {
        return new Airtouch5ConnectorThread(input, responseCallback);
    }

}
