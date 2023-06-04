package airtouch.v5.model;

import java.util.List;

public class ConsoleVersionResponse {

    private boolean updateRequired;
    private List<String> versions;

    public boolean isUpdateRequired() {
        return updateRequired;
    }
    public void setUpdateRequired(boolean updateRequired) {
        this.updateRequired = updateRequired;
    }
    public List<String> getVersions() {
        return versions;
    }
    public void setVersions(List<String> versions) {
        this.versions = versions;
    }
    @Override
    public String toString() {
        return "ConsoleVersionResponse [updateRequired=" + updateRequired + ", versions=" + versions + "]";
    }

}
