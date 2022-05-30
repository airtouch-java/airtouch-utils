package airtouch.v4;


import airtouch.v4.model.GroupStatus;

public interface Airtouch {

    public ResponseList<GroupStatus> getGroupStatus(int messageId);

}
