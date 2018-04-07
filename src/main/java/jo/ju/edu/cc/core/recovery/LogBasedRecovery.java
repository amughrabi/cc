package jo.ju.edu.cc.core.recovery;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/*
* A log is kept on stable storage.
*   --> Contains a sequence of log records, described as follows.
**/
public class LogBasedRecovery {
    private Map<String, List<LogEntry>> log;
    public LogBasedRecovery() {
        // the order of the transactions is important!
        log = new LinkedHashMap<>();
    }

    public void log(@NotNull String transactionId, @NotNull LogEntry entry) {
        List<LogEntry> entries = log.get(transactionId);
        if(entries == null) {
            entries = new ArrayList<>();
        }
        entries.add(entry);
        log.put(transactionId, entries);
    }

    public void log(@NotNull String transactionId, @NotNull String variable, @NotNull String oldValue,
                    @NotNull String newValue) {
        LogEntry entry = new LogEntry();
        entry.put("transactionId", transactionId);
        entry.put("var", variable);
        entry.put("oldValue", oldValue);
        entry.put("newValue", newValue);

        log(transactionId, entry);
    }
}
