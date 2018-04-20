package jo.ju.edu.cc.core.transactions;

import jo.ju.edu.cc.core.recovery.LogBasedRecovery;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Snapshot {
    private Disk disk;
    private Buffer buffer;
    private Registers registers;
    private FailuresTable failuresTable;
    private Protocol protocol;

    private List<Transaction> transactions;
    private LogBasedRecovery logBasedRecovery;

    private TimeFrameTable timeFrameTable;

    public Snapshot(@NotNull Disk disk, @NotNull List<Transaction> transactions) {
        this.disk = disk;
        this.transactions = transactions;
        logBasedRecovery = new LogBasedRecovery();
        buffer = new Buffer();
        registers = new Registers();
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

    public LogBasedRecovery getLogBasedRecovery() {
        return logBasedRecovery;
    }

    public void setLogBasedRecovery(LogBasedRecovery logBasedRecovery) {
        this.logBasedRecovery = logBasedRecovery;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    public Registers getRegisters() {
        return registers;
    }

    public void setRegisters(Registers registers) {
        this.registers = registers;
    }

    public FailuresTable getFailuresTable() {
        return failuresTable;
    }

    public void setFailuresTable(FailuresTable failuresTable) {
        this.failuresTable = failuresTable;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public TimeFrameTable getTimeFrameTable() {
        return timeFrameTable;
    }

    public void setTimeFrameTable(TimeFrameTable timeFrameTable) {
        this.timeFrameTable = timeFrameTable;
    }
}
