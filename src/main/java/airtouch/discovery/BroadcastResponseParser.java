package airtouch.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import airtouch.AirtouchVersion;
import airtouch.v4.constant.ConnectionConstants;
import airtouch.discovery.BroadcastResponseCallback.BroadcastResponse;

public class BroadcastResponseParser {
    private BroadcastResponseParser() {} // Prevent instantiation.
    private static final Logger log = LoggerFactory.getLogger(BroadcastResponseParser.class);
    private static final String AIRTOUCH4_VERSION = ConnectionConstants.AIRTOUCH_VERSION_IDENTIFIER;

    public static BroadcastResponse parse(String broadcastResponseStr) {
        log.info("Response: {}", broadcastResponseStr);
        String[] responseArray = broadcastResponseStr.split(",");
        log.info("{}", responseArray.length);
        if (responseArray.length == 4 && AIRTOUCH4_VERSION.equals(responseArray[2])) {
            log.info("length: {}, IP: {}, MAC: {}, Version: {}, ID: {}", 
                    responseArray.length,
                    responseArray[0],
                    responseArray[1],
                    responseArray[2],
                    responseArray[3]);
            
            return new BroadcastResponse() {
                
                @Override
                public String getHostAddress() {
                    return responseArray[0];
                }
                
                @Override
                public String getMacAddress() {
                    return responseArray[1];
                }
                
                @Override
                public AirtouchVersion getAirtouchVersion() {
                    switch (responseArray[2]) {
                        case AIRTOUCH4_VERSION:
                            return AirtouchVersion.AIRTOUCH4;
                        default:
                            return null;
                    }
                }
                
                @Override
                public String getAirtouchId() {
                    return responseArray[3];
                }

                @Override
                public Integer getPortNumber() {
                    switch (responseArray[2]) {
                        case AIRTOUCH4_VERSION:
                            return AirtouchVersion.AIRTOUCH4.getListeningPort();
                        default:
                            return null;
                    }
                }
            };
        }
        return null;
    }

}
