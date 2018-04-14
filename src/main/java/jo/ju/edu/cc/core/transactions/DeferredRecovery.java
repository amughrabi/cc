package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;

public class DeferredRecovery extends RecoveryMethod {
    @Override
    public @NotNull Snapshot undo(@NotNull Snapshot snapshot, @NotNull String transactionId) {
        // Do nothing, all information persist in the buffer and registers, Database is in consistent state.
        return snapshot;
    }
}
