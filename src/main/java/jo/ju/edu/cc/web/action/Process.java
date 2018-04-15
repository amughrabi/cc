package jo.ju.edu.cc.web.action;

import com.opensymphony.xwork2.ActionSupport;
import jo.ju.edu.cc.core.transactions.Protocol;
import jo.ju.edu.cc.core.transactions.Snapshot;
import jo.ju.edu.cc.core.transactions.TimeFrameTable;
import jo.ju.edu.cc.core.transactions.TransactionsManager;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

public class Process extends ActionSupport {
    private String snapshotXML;
    private String recoveryMethod;

    @Override
    public String execute() {
        HttpServletRequest request = ServletActionContext.getRequest();
        try {
            Snapshot snapshot = TransactionsManager.load(snapshotXML);

            TimeFrameTable timeFrameTable = TransactionsManager.constructTimeFrameTable(snapshot);

            request.setAttribute("snapshot", snapshot);
            request.setAttribute("timeFrameTable", timeFrameTable);

            snapshot = TransactionsManager.execute(snapshot, timeFrameTable, Protocol.LOG_BASED_IMMEDIATE);
            request.setAttribute("snapshotAfter", snapshot);
        } catch (Exception ex) {
            request.setAttribute("error", "Sorry, wrong XML format");
        }
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