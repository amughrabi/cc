package jo.ju.edu.cc.core.locking;

import jo.ju.edu.cc.core.transactions.TimeFrameTable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Strict2PL {
    private TimeFrameTable timeFrameTable;
    public Strict2PL(@NotNull TimeFrameTable timeFrameTable) {
        this.timeFrameTable = timeFrameTable;
    }

    // A transaction may release locks, but may not obtain any new locks
    public void shrinking() {

    }

    // A transaction may obtain locks, but may not release any lock
    public void growing() {
        Map<String, String> lockTable = new HashMap<>();
        //for(timeFrameTable.getTable().keySet().size())
    }

    public TimeFrameTable getTimeFrameTable() {
        return timeFrameTable;
    }

    public void setTimeFrameTable(TimeFrameTable timeFrameTable) {
        this.timeFrameTable = timeFrameTable;
    }
}
