package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;

public class Buffer extends Disk {
    @Override
    public @NotNull String getName() {
        return "buffer";
    }
}
