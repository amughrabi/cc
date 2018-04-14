package jo.ju.edu.cc.core.transactions;

import jo.ju.edu.cc.core.recovery.LogEntry;
import jo.ju.edu.cc.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class RecoveryMethod {

    public @NotNull Snapshot redo(@NotNull Snapshot snapshot, @NotNull String transactionId) {
        return perform(snapshot, transactionId, false);
    }

    public @NotNull Snapshot undo(@NotNull Snapshot snapshot, @NotNull String transactionId) {
        return perform(snapshot, transactionId, true);
    }

    public @NotNull Snapshot perform(@NotNull Snapshot snapshot, @NotNull String transactionId, boolean useOldVal) {
        List<LogEntry> entries =  snapshot.getLogBasedRecovery().getLog().get(transactionId);
        Disk disk = snapshot.getDisk();
        for(LogEntry entry : entries) {
            if(!StringUtil.isEqual(entry.getVariable(), IOperation.START) &&
                    !StringUtil.isEqual(entry.getVariable(), IOperation.COMMIT)) {
                Block block = new Block();
                block.put("id", entry.getVariable());
                block.setValue( useOldVal ? entry.getOldValue() : entry.getNewValue() );
                disk.addOrUpdateBlock(block);
            }
        }
        snapshot.setDisk(disk);
        return snapshot;
    }
}
