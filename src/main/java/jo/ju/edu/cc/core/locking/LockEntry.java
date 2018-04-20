package jo.ju.edu.cc.core.locking;

import jo.ju.edu.cc.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class LockEntry {
    private String transactionId, variable, lockType;

    public LockEntry(@NotNull String transactionId, @NotNull String variable, @NotNull String lockType) {
        this.variable = variable;
        this.lockType = lockType;
        this.transactionId = transactionId;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(@NotNull String variable) {
        this.variable = variable;
    }

    public @NotNull String getLockType() {
        return lockType;
    }

    public void setLockType(@NotNull String lockType) {
        this.lockType = lockType;
    }

    public @NotNull String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(@NotNull String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isSharedLock() {
        return StringUtil.isEqual(getLockType(), LockType.shared);
    }

    public boolean isExclusiveLock() {
        return StringUtil.isEqual(getLockType(), LockType.exclusive);
    }
}
