package jo.ju.edu.cc.core.transactions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Snapshot {
    private Disk disk;
    private List<Transaction> transactions;

    public Snapshot(@NotNull Disk disk, @NotNull List<Transaction> transactions) {
        this.disk = disk;
        this.transactions = transactions;
    }

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
