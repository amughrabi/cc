package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class Operation extends Attributes implements IOperation {
    private String value, transactionId;

    public void put(@NotNull String key, @NotNull String value) {
        getAttributes().put(key, value);
    }

    public @Nullable String get(@NotNull String  key) {
        return getAttributes().get(key);
    }

    public @NotNull String getType() {
        return getAttributes().get("type");
    }

    public @NotNull String getVariable() {
        return getAttributes().get("var");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public @NotNull JSONObject toJSON() {
        JSONObject operationsObject = new JSONObject();
        operationsObject.put("transactionId", getTransactionId());
        operationsObject.put("type",          getType());
        operationsObject.put("var",           getVariable());
        operationsObject.put("value",         getValue());
        return  operationsObject;
    }
}
