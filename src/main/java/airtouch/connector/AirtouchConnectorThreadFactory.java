package airtouch.connector;

import java.io.InputStream;

import airtouch.ResponseCallback;

public interface AirtouchConnectorThreadFactory {

    AirtouchConnectorThread create(InputStream input, ResponseCallback responseCallback);
}
