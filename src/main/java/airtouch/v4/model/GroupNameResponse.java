package airtouch.v4.model;

import airtouch.v4.constant.MessageConstants.ExtendedMessageType;

public class GroupNameResponse {
    
    private ExtendedMessageType messageType = ExtendedMessageType.GROUP_NAME;
    private int groupNumber;
    private String name;
    
    public ExtendedMessageType getMessageType() {
        return messageType;
    }
    
    public int getGroupNumber() {
        return groupNumber;
    }
    
    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GroupName [messageType=" + messageType + ", groupNumber=" + groupNumber + ", name=" + name + "]";
    }
    
    
}
