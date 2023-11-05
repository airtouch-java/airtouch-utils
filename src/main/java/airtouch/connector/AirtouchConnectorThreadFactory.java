package airtouch.connector;

import java.io.InputStream;

import airtouch.ResponseCallback;

public interface AirtouchConnectorThreadFactory<T> {

    AirtouchConnectorThread<T> create(InputStream input, ResponseCallback responseCallback);
}
