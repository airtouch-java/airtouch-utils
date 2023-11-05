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

    /**
     * For Airtouch4
     * @return value
     */
    public int getMinSetPoint() {
        return minSetPoint;
    }
    
    /**
     * For Airtouch4
     */
    public void setMinSetPoint(int minSetPoint) {
        this.minSetPoint = minSetPoint;
    }
    
    /**
     * For Airtouch4
     * @return value
     */
    public int getMaxSetPoint() {
        return maxSetPoint;
    }
    
    /**
     * For Airtouch4
     */
    public void setMaxSetPoint(int maxSetPoint) {
        this.maxSetPoint = maxSetPoint;
    }
    /**
     * For Airtouch5
     * @return value
     */
    public void setMinCoolSetPoint(int minCoolSetPoint) {
        this.minCoolSetPoint = minCoolSetPoint;
    }
    /**
     * For Airtouch5
     * @return value
     */
    public int getMinCoolSetPoint() {
        return minCoolSetPoint;
    }
    public void setMaxCoolSetPoint(int maxCoolSetPoint) {
        this.maxCoolSetPoint = maxCoolSetPoint;
    }
    /**
     * For Airtouch5
     * @return value
     */
    public int getMaxCoolSetPoint() {
        return maxCoolSetPoint;
    }
    /**
     * For Airtouch5
     * @return value
     */
    public int getMinHeatSetPoint() {
        return minHeatSetPoint;
    }
    /**
     * For Airtouch5
     */
    public void setMinHeatSetPoint(int minHeatSetPoint) {
        this.minHeatSetPoint = minHeatSetPoint;
    }
    /**
     * For Airtouch5
     * @return value
     */
    public int getMaxHeatSetPoint() {
        return maxHeatSetPoint;
    }
    /**
     * For Airtouch5
     */
    public void setMaxHeatSetPoint(int maxHeatSetPoint) {
        this.maxHeatSetPoint = maxHeatSetPoint;
    }
    
    @Override
    public String toString() {
        return "AirConditionerAbilityResponse [acNumber=" + acNumber + ", acName=" + acName + ", startZoneNumber="
                + startZoneNumber + ", zoneCount=" + zoneCount + ", supportedModes=" + supportedModes
                + ", supportedFanSpeeds=" + supportedFanSpeeds + ", minSetPoint=" + minSetPoint + ", maxSetPoint="
                + maxSetPoint + ", minCoolSetPoint=" + minCoolSetPoint + ", maxCoolSetPoint=" + maxCoolSetPoint
                + ", minHeatSetPoint=" + minHeatSetPoint + ", maxHeatSetPoint=" + maxHeatSetPoint + "]";
    }
}
