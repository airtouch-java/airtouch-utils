package airtouch.model;

public class AirConditionerErrorResponse {
    private int acNumber;
    private boolean isErrored;
    private String errorMessage;

    public int getAcNumber() {
        return acNumber;
    }
    public void setAcNumber(int acNumber) {
        this.acNumber = acNumber;
    }

    public boolean isErrored() {
        return isErrored;
    }
    public void setErrored(boolean isErrored) {
        this.isErrored = isErrored;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
