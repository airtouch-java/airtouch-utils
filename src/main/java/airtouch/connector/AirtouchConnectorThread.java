package airtouch.connector;

public interface AirtouchConnectorThread<T> {

    void start();
    void shutdown();
    boolean isRunning();



}
