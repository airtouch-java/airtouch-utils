package airtouch.model;

import java.util.HashSet;
import java.util.Set;

import airtouch.constant.AirConditionerControlConstants;
import airtouch.constant.AirConditionerControlConstants.FanSpeed;
import airtouch.constant.AirConditionerControlConstants.Mode;

public class AirConditionerAbilityResponse {

    private int acNumber;
    private String acName;
    private int startZoneNumber;
    private int zoneCount;
    private Set<AirConditionerControlConstants.Mode> supportedModes = new HashSet<>();
    private Set<AirConditionerControlConstants.FanSpeed> supportedFanSpeeds = new HashSet<>();

    private int minSetPoint;
    private int maxSetPoint;
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
        return startZoneNumber;
    }
    public void setStartGroupNumber(int startGroupNumber) {
        this.startZoneNumber = startGroupNumber;
    }

    public int getZoneCount() {
        return zoneCount;
    }
    public void setZoneCount(int zoneCount) {
        this.zoneCount = zoneCount;
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

    public int getMinSetPoint() {
        return minSetPoint;
    }
    public void setMinSetPoint(int minSetPoint) {
        this.minSetPoint = minSetPoint;
    }
    public int getMaxSetPoint() {
        return maxSetPoint;
    }
    public void setMaxSetPoint(int maxSetPoint) {
        this.maxSetPoint = maxSetPoint;
    }
    @Override
    public String toString() {
        return "AirConditionerAbilityResponse [acNumber=" + acNumber + ", acName=" + acName + ", startGroupNumber="
                + startZoneNumber + ", groupCount=" + zoneCount + ", supportedModes=" + supportedModes
                + ", supportedFanSpeeds=" + supportedFanSpeeds + ", minSetPoint=" + minSetPoint + ", maxSetPoint="
                + maxSetPoint + "]";
    }


}
