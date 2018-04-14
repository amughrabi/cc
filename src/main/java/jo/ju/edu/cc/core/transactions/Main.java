package jo.ju.edu.cc.core.transactions;

import jo.ju.edu.cc.core.recovery.LogBasedRecovery;
import jo.ju.edu.cc.core.recovery.LogEntry;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String... args) throws Exception {

        Snapshot snapshot = TransactionsManager.load("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<snapshot>\n" +
                "    <!--\n" +
                "    Read(A)\n" +
                "    A = A - 50\n" +
                "    Write(A)\n" +
                "    Read(B)\n" +
                "    B = B+50\n" +
                "    Write(B)\n" +
                "    -->\n" +
                "    <disk>\n" +
                "        <block id=\"A\">10</block>\n" +
                "        <block id=\"B\">9</block>\n" +
                "    </disk>\n" +
                "\n" +
                "    <transaction id=\"T1\" accessTimeUnit=\"10\">\n" +
                "        <operation var=\"start\"   type=\"start\"/>\n" +
                "        <operation var=\"A\"       type=\"read\"/>\n" +
                "        <operation var=\"A\"       type=\"sub\">50</operation>\n" +
                "        <operation var=\"A\"       type=\"write\"/>\n" +
                "        <operation var=\"failure\" type=\"failure\"/>\n" +
                "        <operation var=\"B\"       type=\"read\"/>\n" +
                "        <operation var=\"B\"       type=\"add\">50</operation>\n" +
                "        <operation var=\"B\"       type=\"write\"/>\n" +
                "        <operation var=\"commit\"  type=\"commit\"/>\n" +
                "    </transaction>\n" +
                "\n" +
                "    <transaction id=\"T2\" accessTimeUnit=\"5\">\n" +
                "        <operation var=\"start\"   type=\"start\"/>\n" +
                "        <operation var=\"A\"       type=\"read\"/>\n" +
                "        <operation var=\"A\"       type=\"sub\">50</operation>\n" +
                "        <operation var=\"A\"       type=\"write\"/>\n" +
                "        <operation var=\"B\"       type=\"read\"/>\n" +
                "        <operation var=\"B\"       type=\"add\">50</operation>\n" +
                "        <operation var=\"B\"       type=\"write\"/>\n" +
                "        <operation var=\"commit\"  type=\"commit\"/>\n" +
                "    </transaction>\n" +
                "</snapshot>");

        for(Transaction transaction: snapshot.getTransactions()) {
            System.out.println(transaction);
        }

        for(Block block : snapshot.getDisk().getBlocks()) {
            System.out.println(block);
        }

        System.out.println(snapshot.getDisk());

        TimeFrameTable timeFrameTable = TransactionsManager.constructTimeFrameTable(snapshot);

        Map<Long, List<Operation>> table = timeFrameTable.getTable();
        List<Operation> ops;
        System.out.println("------------------------------------------------");
        for(long tUnit : timeFrameTable.getTable().keySet()) {
            ops = table.get(tUnit);
            System.out.print("|");
            System.out.print(tUnit);
            System.out.print("|");
            for(Operation operation : ops) {
                System.out.print(operation);
                System.out.print("|");
            }
            System.out.println("");
            System.out.println("------------------------------------------------");
        }

        snapshot = TransactionsManager.execute(snapshot, timeFrameTable, Protocol.LOG_BASED_IMMEDIATE);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        Buffer buffer = snapshot.getBuffer();
        System.out.println(buffer);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        Disk disk = snapshot.getDisk();
        System.out.println(disk);

        LogBasedRecovery logBasedRecovery = snapshot.getLogBasedRecovery();
        Map<String, List<LogEntry>> logMap = logBasedRecovery.getLog();
        for(String transactionId : logMap.keySet()) {
            System.out.println(transactionId);
            System.out.println("---------------------------------------------------------------------");
            List<LogEntry> entries = logMap.get(transactionId);
            for(LogEntry entry : entries) {
                System.out.println(entry);
            }
        }

    /*    FailuresTable failures = new FailuresTable();
        failures.markAsFailed("T1");
        System.out.println(failures.isFailed("blahBlah"));
        System.out.println(failures.isFailed("T1"));*/

        if(RecoveryManager.requireRecover(snapshot)) {
            snapshot = RecoveryManager.recover(snapshot);
            disk = snapshot.getDisk();
            System.out.println(disk);
        }
    }
}
