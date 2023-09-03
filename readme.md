## airtouch-utils Java Library for the Polyaire AirTouch Air-conditioning system

AirTouch is a third party unit for controlling a home Air-conditioning system. For more information see: [AirTouch](https://www.airtouch.net.au/)

The developer of this library is not associated with the Polyaire company or the AirTouch product other than to have an AirTouch unit installed at home.

The AirTouch unit is an Android tablet device and some integration hardware installed in the home. The hardware integrates with various make of ducted home air-conditioners and the tablet runs the AirTouch UI and service.

This library `airtouch-utils` connects to the AirTouch service (over TCP) running on the Android tablet. The service supports UDP broadcast discovery and allows TCP connections to query and control the AirTouch system.

`airtouch-utils` currently supports AirTouch4 (well tested), and AirTouch5 (un-tested).

### Features of `airtouch-utils`
- A discovery service and callback to broadcast the required UDP packets on the WiFi network in an attacmpt to discover the AirTouch system
- Java implementation of ModBus over TCP protocol that the AirTouch uses for communication.

### Using the `airtouch-utils` library

AirTouch4 and AirTouch5 support are represented by different packages within this project.
You must create an instance of the correct class for dealing with the version of AirTouch you want to talk to.

#### Connecting
Connect to the Airtouch to send messages. Message responses are sent asynchronously from the Airtouch in response to a status request, or when an item changes state in the AirTouch (eg, temperature changes, power on/off, damper percentage changes).

```java
    AirtouchConnector airtouchConnector = new AirtouchConnector(this.hostName, this.portNumber, new ResponseCallback() {
      @SuppressWarnings("rawtypes")
      public void handleResponse(Response response) {
        // Write some code here to handle the response received from AirTouch
      }
    });
    airtouchConnector.start();
```
You should expect `ResponseCallback.handleResponse(Response response)` to be called for any response event, and would typically code a switch statement to handle the type of message in the callback.

#### Sending a message to AirTouch
`artouch-utils` contains a number of [handlers](./src/main/airtouch.v4.handler/) that do the job of creating request messages for the AirTouch and for parsing the responses received from AirTouch.

```	// Example send a request to get the Group statuses
    // When sending a message to AirTouch, you can include a request ID. The response
    // will contain the same request id.
    // For the GroupStatusHandler, you pass in a group index (zero based) or null to request the status for all groups.
    airtouchConnector.sendRequest(GroupStatusHandler.generateRequest(nextRequestId, null));
```

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

###Building
Using Java8 (or higher) run maven as follows:

```
mvn clean test package install
```
