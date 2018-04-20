package jo.ju.edu.cc.core.transactions;

import jo.ju.edu.cc.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class Operation extends Attributes implements IOperation {
    private String value, transactionId, lockType;

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

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    @Override
    public @NotNull JSONObject toJSON() {
        JSONObject operationsObject = new JSONObject();
        operationsObject.put("transactionId", getTransactionId());
        operationsObject.put("type",          getType());
        operationsObject.put("var",           getVariable());
        operationsObject.put("value",         getValue());
        operationsObject.put("lockType", getLockType());
        return  operationsObject;
    }

    public boolean isNull() {
        return this instanceof NullOperation;
    }

    public boolean isRead() {
        return StringUtil.isEqual(getType(), READ);
    }

    public boolean isWrite() {
        return StringUtil.isEqual(getType(), WRITE);
    }

    public boolean isCommit() {
        return StringUtil.isEqual(getType(), COMMIT);
    }

    public boolean isStart() {
        return StringUtil.isEqual(getType(), START);
    }

    public boolean isMath() {
        return StringUtil.isEqual(getType(), ADD) ||
                StringUtil.isEqual(getType(), SUB) ||
                StringUtil.isEqual(getType(), DIVIDE) ||
                StringUtil.isEqual(getType(), MULTI);
    }

    public boolean isFailure() {
        return StringUtil.isEqual(getType(), FAILURE);
    }

    public boolean isWait() {
        return this instanceof WaitOperation;
    }
}
