package airtouch.v5.model;

import java.util.HashSet;
import java.util.Set;

import airtouch.v5.constant.AirConditionerControlConstants;
import airtouch.v5.constant.AirConditionerControlConstants.FanSpeed;
import airtouch.v5.constant.AirConditionerControlConstants.Mode;

public class AirConditionerAbilityResponse {

    private int acNumber;
    private String acName;
    private int startGroupNumber;
    private int groupCount;
    private Set<AirConditionerControlConstants.Mode> supportedModes = new HashSet<>();
    private Set<AirConditionerControlConstants.FanSpeed> supportedFanSpeeds = new HashSet<>();

    private int minCoolSetPoint;
    private int maxCoolSetPoint;
    private int minHeatSetPoint;
    private int maxHeatSetPoint;
    public int getAcNumber() {
        return acNumber;
    }
    public void setAcNumber(int acNumber) {
        this.acNumber = acNumber;
    }

    public String getAcName() {
        return acName;
    }
    public void setAcName(String acName) {
        this.acName = acName;
    }

    public int getStartGroupNumber() {
        return startGroupNumber;
    }
    public void setStartGroupNumber(int startGroupNumber) {
        this.startGroupNumber = startGroupNumber;
    }

    public int getGroupCount() {
        return groupCount;
    }
    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public boolean addSupportedMode(AirConditionerControlConstants.Mode mode) {
        return this.supportedModes.add(mode);
    }
    public Set<Mode> getSupportedModes() {
        return this.supportedModes;
    }
    public Set<AirConditionerControlConstants.FanSpeed> getSupportedFanSpeeds() {
        return supportedFanSpeeds;
    }
    public boolean addSupportedFanSpeed(FanSpeed fanSpeed) {
        return this.supportedFanSpeeds.add(fanSpeed);
    }

    public int getMinCoolSetPoint() {
        return minCoolSetPoint;
    }
    public void setMinCoolSetPoint(int minSetPoint) {
        this.minCoolSetPoint = minSetPoint;
    }
    public int getMaxCoolSetPoint() {
        return maxCoolSetPoint;
    }
    public void setMaxCoolSetPoint(int maxSetPoint) {
        this.maxCoolSetPoint = maxSetPoint;
    }
    
    public int getMinHeatSetPoint() {
        return minHeatSetPoint;
    }
    public void setMinHeatSetPoint(int minSetPoint) {
        this.minHeatSetPoint = minSetPoint;
    }
    public int getMaxHeatSetPoint() {
        return maxHeatSetPoint;
    }
    public void setMaxHeatSetPoint(int maxSetPoint) {
        this.maxHeatSetPoint = maxSetPoint;
    }
    @Override
    public String toString() {
        return "AirConditionerAbilityResponse [acNumber=" + acNumber + ", acName=" + acName + ", startGroupNumber="
                + startGroupNumber + ", groupCount=" + groupCount + ", supportedModes=" + supportedModes
                + ", supportedFanSpeeds=" + supportedFanSpeeds + ", minCoolSetPoint=" + minCoolSetPoint + ", maxCoolSetPoint="
                + maxCoolSetPoint + " , minHeatSetPoint=" + minHeatSetPoint + ", maxHeatSetPoint=" + maxHeatSetPoint + "]";
    }


}
