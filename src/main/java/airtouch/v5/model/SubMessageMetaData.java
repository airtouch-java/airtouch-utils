package airtouch.v5.model;

import airtouch.v5.constant.MessageConstants.ControlOrStatusMessageSubType;

public class SubMessageMetaData {

    private ControlOrStatusMessageSubType subMessageType;
    private int normalDataLength;
    private int eachRepeatDataLength;
    private int repeatDataCount;

    public ControlOrStatusMessageSubType getSubMessageType() {
        return subMessageType;
    }
    public void setSubType(ControlOrStatusMessageSubType fromByte) {
        this.subMessageType = fromByte;
        
    }

    public int getNormalDataLength() {
        return normalDataLength;
    }
    public void setNormalDataLength(int int1) {
        this.normalDataLength = int1;
        
    }

    public int getEachRepeatDataLength() {
        return eachRepeatDataLength;
    }
    public void setEachRepeatDataLength(int int1) {
        this.eachRepeatDataLength = int1;
    }

    public int getRepeatDataCount() {
        return repeatDataCount;
    }
    public void setRepeatDataCount(int int1) {
        this.repeatDataCount = int1;
        
    }

}
