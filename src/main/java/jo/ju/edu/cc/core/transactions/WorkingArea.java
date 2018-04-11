package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;

public class WorkingArea extends Disk {
    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public @NotNull String getName() {
        return "workingArea";
    }
}
