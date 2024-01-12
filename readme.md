## airtouch-utils Java Library for the Polyaire AirTouch Air-conditioning system

AirTouch is a third party unit for controlling a home Air-conditioning system. For more information see: [AirTouch](https://www.airtouch.net.au/)

The developer of this library is not associated with the Polyaire company or the AirTouch product other than to have an AirTouch unit installed at home. However, I'd like to thank the team at Polyaire for the support they have provided whilst developing the library.

The AirTouch unit is an Android tablet device and some integration hardware installed in the home. The hardware integrates with various make of ducted home air-conditioners and the tablet runs the AirTouch UI and service.

This library `airtouch-utils` connects to the AirTouch service (over TCP) running on the Android tablet. The service supports UDP broadcast discovery and allows TCP connections to query and control the AirTouch system.

`airtouch-utils` currently supports AirTouch4 (well tested), and AirTouch5 (un-tested).

### Features of `airtouch-utils`
- A discovery service and callback to broadcast the required UDP packets on the WiFi network in an attempt to discover the AirTouch system
- Handlers to generate status (read) requests and handle responses for the following:
    - Air Conditioner (Power, Mode, Fan Speed, Temp. Setpoint, Error state)
    - Air Conditioner Ability (Name, Zone count, Supported modes and fan-speeds, min and max setpoints)
    - Zone (Temp. Setpoint, Damper Percentage, Control Mode - damper vs temperature, Temp. Sensor installed, Temp. Sensor battery low)
    - Zone Names (Names of the zones as configured in AirTouch)
    - Console information (Console version, update available)
- Handlers to generate control (update) requests and handle responses for the following:
    - Air Conditioner (Power, Mode, Fan Speed, Setpoint)
    - Zone (Temp. Setpoint, Damper Percentage, Control Mode - damper vs temperature

### Using the `airtouch-utils` library

AirTouch4 and AirTouch5 support are represented by different packages within this project.
You must create an instance of the correct class for dealing with the version of AirTouch you want to talk to.

#### Connecting
Connect to the Airtouch to send messages. Message responses are sent asynchronously from the Airtouch in response to a status request, or when an item changes state in the AirTouch (eg, temperature changes, power on/off, damper percentage changes).

```java
    import airtouch.v4.constant.MessageConstants;
    ...
    
    // AirtouchConnector is generic, so we use a ThreadFactory to create the relevant thread (Airtouch4 or 5)
    AirtouchConnectorThreadFactory threadFactory = new Airtouch4ConnectorThreadFactory();
    
    // MessageConstants.Address is the type of address header. These are different for Airtouch4 and 5
    // so we need to pass in the correct type for our Airtouch version. 
    AirtouchConnector<MessageConstants.Address> airtouchConnector = new AirtouchConnector<>(threadFactory, this.hostName, this.portNumber, new ResponseCallback() {
        public void handleResponse(Response response) {
            // Write some code here to handle the response received from AirTouch
        }
    });
    airtouchConnector.start();
```
You should expect `ResponseCallback.handleResponse(Response response)` to be called for any response event, and would typically code a switch statement to handle the type of message in the callback.

#### Sending a message to AirTouch
`artouch-utils` contains a number of [handlers](src/main/java/airtouch/v4/handler/) that do the job of creating request messages for the AirTouch and parsing the responses received from AirTouch.

There are two types of Handlers:
1. Status Handlers - For generating a request for a status update and for handling responses to status updates.
2. Control Handlers - For generating a request to change the state of the AirTouch.

Note: A control request will generate a status response. Therefore, the control handlers don't need to handle responses because they are handled by the appropriate status handler.

```java
    // Example send a request to get the Zone statuses (known as Groups in AirTouch4)
    // When sending a message to AirTouch, you can include a request ID. The response
    // will contain the same request id.
    // For the GroupStatusHandler, you pass in a group index (zero based) or null to request the status for all groups.
    airtouchConnector.sendRequest(GroupStatusHandler.generateRequest(nextRequestId, null));
```
Shortly after this is invoked, AirTouch will send a response to the connected TCP port. This will be handled by the listener and then the relevant Status Handler invoked to parse the message. Once successfully parsed, `ResponseCallback.handleResponse(Response response)` will be called for your code to handle the response. In the above you would expect the message to be a Zone (aka Group) status update.

Control messages (to change a state in the AirTouch) are similar. A control message is sent, and then shortly after this is invoked, AirTouch will send a response to the connected TCP port. The response will be a status message showing the updated status of the changed object. For example, a Zone control message will generate an updated Zone Status response.

```java
        // Import the correctly versioned handler for your AirTouch.
        import airtouch.v4.handler.AirConditionerControlHandler;
        
        // Create control request to turn off second Air Conditioning unit on AirTouch (zero based index)
        AirConditionerControlRequest acControlRequest = AirConditionerControlHandler.requestBuilder()
                .acNumber(1)
                .acPower(AcPower.POWER_OFF)
                .build();
        // Set the messageId to "1", and then convert the acControlRequest into an AirTouch Hex byte array
        // with applicable header information and checksum.
        Request<MessageType, MessageConstants.Address> request = AirConditionerControlHandler.generateRequest(1, acControlRequest);
        // Send the request to the AirTouch (via the connector)
        airtouchConnector.sendRequest(request);
```

Shortly after this is invoked, AirTouch will send a response to the connected TCP port. This will be handled by the listener and then the relevant Status Handler invoked to parse the message. Once successfully parsed, `ResponseCallback.handleResponse(Response response)` to be called for your code to handle the response. In the above you would expect the message to be a Air Conditioner status update.

For more examples of the usage of handlers see the [handler unit tests](src/test/java/airtouch/v4/handler/). Note: These tests are examples of generating the requests and then validating that the message built matches the hex that should be sent to the AirTouch, or parsing the hex message from the AirTouch back into a valid response event.

#### Handling a response from AirTouch
Responses from AirTouch are parsed and then the `Response` object created and the `ResponseCallback.handleResponse(Response response)` is called.

An implementation might look something like this...

```java
    // Switch on the MessageType. Note: there are versions for AirTouch4 and AirTouch5
    switch (response.getMessageType()) {
    case AC_STATUS:
      status.setAcStatuses((List<AirConditionerStatusResponse>) response.getData());
      break;
    case GROUP_STATUS:
      status.setGroupStatuses((List<GroupStatusResponse>) response.getData());
      break;
    case GROUP_NAME:
      status.setGroupNames(
          ((List<GroupNameResponse>) response.getData())
          .stream()
          .collect(Collectors.toMap(GroupNameResponse::getGroupNumber, GroupNameResponse::getName)));
      break;
    case AC_ABILITY:
      status.setAcAbilities(
          ((List<AirConditionerAbilityResponse>) response.getData())
          .stream()
          .collect(Collectors.toMap(AirConditionerAbilityResponse::getAcNumber, r -> r))
          );
      break;
    case CONSOLE_VERSION:
      status.setConsoleVersion((ConsoleVersionResponse) response.getData()
          .stream()
          .findFirst()
          .orElse(null));
      break;
    default:
      break;
    }
```

#### Discovery
The `AirtouchDiscoverer` will create a listener on the local computer and then broadcast the necessary UDP packets to trigger a reply from the AirTouch unit.
The message required for AirTouch is different for the various versions of the Airtouch. For that reason, you need to tell the discoverer which version you want to use, and it will send the relevant message to the relevant broadcast port.
The discoverer will call the `AirtouchDiscoveryBroadcastResponseCallback` when a valid response is received.

Example broadcast to AirTouch4 and the call back implementation just printing the details to stdout.

```java
      // Create and configure a discoverer with a callback
      AirtouchDiscoverer airtouch4Discoverer = new AirtouchDiscoverer(AirtouchVersion.AIRTOUCH4, new AirtouchDiscoveryBroadcastResponseCallback() {
        @Override
        public void handleResponse(AirtouchDiscoveryBroadcastResponse response) {
          System.out.println(String.format("Found '%s' at '%s' with id '%s'",
              response.getAirtouchVersion(),
              response.getHostAddress(),
              response.getAirtouchId()));
        }
      });
      // Start the discovery thread
      airtouch4Discoverer.start();
```

Note: The discoverer will broadcast forever, so it you should call `airtouch4Discoverer.shutdown();` whilst handling the response.

### Building
Using Java8 (or higher) run maven as follows:

```
mvn clean test package install
```
