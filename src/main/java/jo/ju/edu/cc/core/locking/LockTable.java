package jo.ju.edu.cc.core.locking;

import jo.ju.edu.cc.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LockTable {
    private Map<String, List<LockEntry>> lockTable;

    public LockTable() {
        this.lockTable = new LinkedHashMap<>();
    }

    public void put(@NotNull String transactionId, @NotNull String variable, @NotNull String lockType) {
        List<LockEntry> entries = lockTable.get(transactionId);
        if(entries == null) {
            entries = new ArrayList<>();
        }
        Optional<LockEntry> entry = entries.stream().filter(e -> StringUtil.isEqual(e.getVariable(), variable)).findAny();
        if(!entry.isPresent()) {
            entries.add(new LockEntry(transactionId, variable, lockType));
        }
        lockTable.put(transactionId, entries);
    }

    public @Nullable LockEntry get(@NotNull String transactionId, @NotNull  String variable) {
        List<LockEntry> entries = lockTable.get(transactionId);
        if(entries != null) {
            Optional<LockEntry> entry = entries.stream().filter(e -> StringUtil.isEqual(e.getVariable(), variable)).findAny();
            return entry.orElse(null);
        }
        return null;
    }

    public Map<String, List<LockEntry>> getLockTable() {
        return lockTable;
    }

    public void setLockTable(Map<String, List<LockEntry>> lockTable) {
        this.lockTable = lockTable;
    }

    public void clear() {
        lockTable.clear();
    }

    public void removeLock(@NotNull String transactionId) {
        lockTable.remove(transactionId);
    }
}
