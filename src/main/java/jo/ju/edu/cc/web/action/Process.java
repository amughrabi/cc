package jo.ju.edu.cc.web.action;

import com.opensymphony.xwork2.ActionSupport;

public class Process extends ActionSupport {
    private String snapshotXML;
    private String recoveryMethod;
    private String useStrict2PL;

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

    public String getUseStrict2PL() {
        return useStrict2PL;
    }

    public void setUseStrict2PL(String useStrict2PL) {
        this.useStrict2PL = useStrict2PL;
    }
}