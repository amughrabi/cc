package jo.ju.edu.cc.web.action;

import com.opensymphony.xwork2.ActionSupport;

public class Process extends ActionSupport {
    private String snapshotXML;
    private String recoveryMethod;

    @Override
    public String execute() {
        return SUCCESS;
    }

    public String getSnapshotXML() {
        return snapshotXML;
    }

    public void setSnapshotXML(String snapshotXML) {
        this.snapshotXML = snapshotXML;
    }

    public String getRecoveryMethod() {
        return recoveryMethod;
    }

    public void setRecoveryMethod(String recoveryMethod) {
        this.recoveryMethod = recoveryMethod;
    }
}