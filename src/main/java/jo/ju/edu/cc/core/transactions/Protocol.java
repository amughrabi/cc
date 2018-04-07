package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;

public enum Protocol {
    LOG_BASED_DEFERRED {
        @Override
        public @NotNull String toString() {
            return "logBasedDeferred";
        }
    },
    LOG_BASED_IMMEDIATE {
        @Override
        public @NotNull String toString() {
            return "logBasedImmediate";
        }
    }
}
