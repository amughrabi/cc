package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaitOperation extends Operation {

    @Override
    public void put(@NotNull String key, @NotNull String value) {
        // Do nothing
    }

    @Override
    public @Nullable String get(@NotNull String  key) {
        return null;
    }

    @Override
    public @NotNull String getType() {
        return "WAIT";
    }

    @Override
    public @NotNull String getVariable() {
        return "WAIT";
    }

    @Override
    public String getValue() {
        return "WAIT";
    }

    @Override
    public void setValue(String value) {
        // Do nothing
    }
}
