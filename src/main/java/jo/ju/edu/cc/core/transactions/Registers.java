package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

// Every transaction has its own working area in the CPU
public class Registers {
    private Map<String, WorkingArea> registers;
    public Registers() {
        this.registers = new HashMap<>();
    }

    // We just modifying
    public void addOrUpdateBlock(@NotNull String transactioId, @NotNull Block block) {
        WorkingArea workingArea = registers.get(transactioId);
        if(workingArea == null) {
            workingArea = new WorkingArea();
            workingArea.setTransactionId(transactioId);
        }
        workingArea.addOrUpdateBlock(block);

        registers.put(transactioId, workingArea);
    }

    public @Nullable Block getBlock(@NotNull String transactionId, @NotNull String blockId) {
        WorkingArea workingArea = registers.get(transactionId);
        if(workingArea != null) {
            return workingArea.getBlock(blockId);
        }
        return null;
    }
}
