package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Snapshot {
    private Buffer buffer;
    private List<Transaction> transactions;

    public Snapshot(@NotNull Buffer buffer, @NotNull List<Transaction> transactions) {
        this.buffer = buffer;
        this.transactions = transactions;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
