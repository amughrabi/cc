package jo.ju.edu.cc.core.locking;

import jo.ju.edu.cc.core.transactions.Operation;
import jo.ju.edu.cc.core.transactions.TimeFrameTable;
import jo.ju.edu.cc.core.transactions.WaitOperation;
import jo.ju.edu.cc.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class Strict2PL implements LockType {
    private TimeFrameTable timeFrameTable;
    private LockTable lockTable;
    public Strict2PL(@NotNull TimeFrameTable timeFrameTable) {
        this.timeFrameTable = timeFrameTable;
        this.lockTable = new LockTable();
    }

    public void contruct2PLTimeFrame() {
        growing(); // fill the lock table

        boolean changeInSize = true;
        while (changeInSize) {
            changeInSize = false;

            LockTable currentLocks = new LockTable();

            List<Operation> operations;
            for(long timeUnit : timeFrameTable.getTable().keySet()) {
                operations = timeFrameTable.getTable().get(timeUnit);
                for(Operation operation : operations) {
                    if(operation.isRead() || operation.isWrite()) {

                        LockEntry requiredLock = lockTable.get(operation.getTransactionId(), operation.getVariable());
                        if(canLock(currentLocks, operation.getTransactionId(), operation.getVariable(), requiredLock.getLockType())) {
                            currentLocks.put(operation.getTransactionId(), operation.getVariable(), requiredLock.getLockType());
                        } else {
                            // wait
                            timeFrameTable = shift(timeFrameTable, operation.getTransactionId(), timeUnit);
                            changeInSize = true;
                        }

                        operation.setLockType(requiredLock.getLockType());
                    } else if (operation.isCommit()) {
                        currentLocks.removeLock(operation.getTransactionId());
                    }
                }
            }
        }

        shrinking(); // free the lock table
    }

    public @NotNull TimeFrameTable shift(@NotNull TimeFrameTable table, @NotNull String transactionId, @NotNull long timeUnit) {
        TimeFrameTable frameTable = new TimeFrameTable();
        List<Operation> operations;
        for(long tUnit : table.getTable().keySet()) {
            operations = table.getTable().get(tUnit);
            int idx = 0;
            for(Operation operation : operations) {
                if(StringUtil.isEqual(operation.getTransactionId(), transactionId) && timeUnit <= tUnit) {
                    if(timeUnit == tUnit) {
                        WaitOperation waitOperation = new WaitOperation();
                        waitOperation.setTransactionId(operation.getTransactionId());
                        frameTable.put(tUnit, waitOperation, idx);
                    }
                    frameTable.put(tUnit + 1, operation, idx);
                } else {
                    frameTable.put(tUnit, operation, idx);
                }
                idx ++;
            }
        }
        return frameTable;
    }

    private boolean canLock(@NotNull LockTable table, @NotNull String transactionId, @NotNull String variable,
                             @NotNull String targetLockType) {
        /* Conflicting locks are expressed by the compatibility matrix:
        *   |   | S | X |
        *   | S | / | - |
        *   | X | - | - |
        *
        * */
        List<LockEntry> entries;
        for(String transId : table.getLockTable().keySet()) {
            if(!StringUtil.isEqual(transactionId, transId)) {
                entries = table.getLockTable().get(transId);
                if(entries != null && !entries.isEmpty()) {
                    for(LockEntry entry : entries) {
                        if(StringUtil.isEqual(entry.getVariable(), variable)) {
                            return entry.isSharedLock() && StringUtil.isEqual(targetLockType, LockType.shared);
                        }
                    }
                }
            }
        }
        return true;
    }

    // A transaction may release locks, but may not obtain any new locks
    public void shrinking() {
        lockTable.clear();
    }

    // A transaction may obtain locks, but may not release any lock
    public void growing() {
        Map<Long, List<Operation>> table = timeFrameTable.getTable();
        Long[] keys = table.keySet().toArray(new Long[] {});
        for(int i = keys.length -1; i >=0; i--) {
            List<Operation> operations = table.get(keys[i]);
            for(Operation operation : operations) {
                if( operation.isWrite() )
                    lockTable.put(operation.getTransactionId(), operation.getVariable(), exclusive);
                else if( operation.isRead() )
                    lockTable.put(operation.getTransactionId(), operation.getVariable(), shared);
            }
        }
    }

    public TimeFrameTable getTimeFrameTable() {
        return timeFrameTable;
    }

    public void setTimeFrameTable(TimeFrameTable timeFrameTable) {
        this.timeFrameTable = timeFrameTable;
    }

    public LockTable getLockTable() {
        return lockTable;
    }

    public void setLockTable(LockTable lockTable) {
        this.lockTable = lockTable;
    }
}
