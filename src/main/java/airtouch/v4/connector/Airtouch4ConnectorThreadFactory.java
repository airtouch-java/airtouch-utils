package airtouch.v4.connector;

import java.io.InputStream;

import airtouch.ResponseCallback;
import airtouch.connector.AirtouchConnectorThread;
import airtouch.connector.AirtouchConnectorThreadFactory;

public class Airtouch4ConnectorThreadFactory implements AirtouchConnectorThreadFactory {

    @Override
    public AirtouchConnectorThread create(InputStream input, ResponseCallback responseCallback) {
        return new Airtouch4ConnectorThread(input, responseCallback);
    }

}
