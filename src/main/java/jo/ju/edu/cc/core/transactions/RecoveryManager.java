package jo.ju.edu.cc.core.transactions;

import jo.ju.edu.cc.core.recovery.LogBasedRecovery;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecoveryManager {
    public static @NotNull RecoveryMethod getRecoveryMethod(@NotNull Protocol protocol) {
        if(protocol == Protocol.LOG_BASED_IMMEDIATE)
            return new ImmediateRecovery();
        return new DeferredRecovery();
    }

    public static @NotNull Snapshot recover(@NotNull Snapshot snapshot) {
        if(!requireRecover(snapshot)) return snapshot;

        RecoveryMethod recoveryMethod = getRecoveryMethod( snapshot.getProtocol() );
        LogBasedRecovery logBasedRecovery = snapshot.getLogBasedRecovery();
        FailuresTable failuresTable = snapshot.getFailuresTable();

        List<String> redoQueue = new ArrayList<>();
        List<String> undoQueue = new ArrayList<>();

        for(String transactionId : logBasedRecovery.getLog().keySet()) {
            if(failuresTable.isFailed(transactionId)) {
                undoQueue.add(transactionId);
            }
        }

        String[] transactions = logBasedRecovery.getLog().keySet().toArray(new String[] {});
        for(int lastIdx = logBasedRecovery.getLog().size() - 1; lastIdx >= 0; lastIdx --) {
            if(!failuresTable.isFailed(transactions[lastIdx])) {
                redoQueue.add(transactions[lastIdx]);
            }
        }

        for(String transactionId : undoQueue) {
            snapshot = recoveryMethod.undo(snapshot, transactionId);
        }

        for(String transactionId : redoQueue) {
            snapshot = recoveryMethod.redo(snapshot, transactionId);
        }

        return snapshot;
    }

    public static boolean requireRecover(@NotNull Snapshot snapshot) {
        return snapshot.getFailuresTable().hasFailures();
    }
}
