package airtouch.v4;


import airtouch.v4.model.AirConditionerStatusResponse;
import airtouch.v4.model.GroupNameResponse;
import airtouch.v4.model.GroupStatusResponse;

public interface Airtouch {

    public ResponseList<GroupStatusResponse> getGroupStatus(int messageId);
    public ResponseList<GroupNameResponse> getGroupNames(int messageId);
    public ResponseList<AirConditionerStatusResponse> getAirConditionerStatus(int messageId);

}
