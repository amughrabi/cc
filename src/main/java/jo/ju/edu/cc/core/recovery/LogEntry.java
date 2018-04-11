package jo.ju.edu.cc.core.recovery;

import jo.ju.edu.cc.core.transactions.Attributes;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

// <transactionId, variable, oldValue, newValue>
// <T0, X, 50, 100>
public class LogEntry extends Attributes implements ILogEntry {

    public @NotNull String getTransactionId() {
        return getAttributes().get(transactionId);
    }

    public @NotNull String getVariable() {
        return getAttributes().get(variable);
    }

    public @NotNull String getOldValue() {
        return getAttributes().get(oldValue);
    }

    public @NotNull String getNewValue() {
        return getAttributes().get(newValue);
    }

    @Override
    public @NotNull JSONObject toJSON() {
        JSONObject entry = new JSONObject();
        entry.put(transactionId, getTransactionId());
        entry.put(variable, getVariable());
        entry.put(oldValue, getOldValue());
        entry.put(newValue, getNewValue());
        return entry;
    }
}
