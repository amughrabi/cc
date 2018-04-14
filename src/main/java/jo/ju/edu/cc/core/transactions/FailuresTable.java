package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FailuresTable {
    private Map<String, Boolean> failures;
    public FailuresTable() {
        failures = new HashMap<>();
    }

    public boolean isFailed(@NotNull String transactionId) {
        return Optional.ofNullable(failures.get(transactionId)).orElse(false);
    }

    public void markAsFailed(@NotNull String transactionId) {
        failures.put(transactionId, true);
    }

    public boolean hasFailures() {
        return !failures.isEmpty();
    }
}
