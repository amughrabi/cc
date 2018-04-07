package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NullOperation extends Operation {

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
        return "NULL";
    }

    @Override
    public @NotNull String getVariable() {
        return "NULL";
    }

    @Override
    public String getValue() {
        return "NULL";
    }

    @Override
    public void setValue(String value) {
        // Do nothing
    }
}
