package jo.ju.edu.cc.core.transactions;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String... args) throws Exception {

        Snapshot snapshot = TransactionsManager.load("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<transactions>\n" +
                "    <!--\n" +
                "    Read(A)\n" +
                "    A = A - 50\n" +
                "    Write(A)\n" +
                "    Read(B)\n" +
                "    B = B+50\n" +
                "    Write(B)\n" +
                "    -->\n" +
                "    <buffer>\n" +
                "        <block id=\"A\">10</block>\n" +
                "        <block id=\"B\">9</block>\n" +
                "    </buffer>\n" +
                "\n" +
                "    <transaction id=\"T1\" accessTimeUnit=\"10\">\n" +
                "        <operation var=\"start\"  type=\"reserved\"/>\n" +
                "        <operation var=\"A\"      type=\"read\"/>\n" +
                "        <operation var=\"A\"      type=\"sub\">50</operation>\n" +
                "        <operation var=\"A\"      type=\"write\"/>\n" +
                "        <operation var=\"B\"      type=\"read\"/>\n" +
                "        <operation var=\"B\"      type=\"add\">50</operation>\n" +
                "        <operation var=\"B\"      type=\"write\"/>\n" +
                "        <operation var=\"commit\" type=\"reserved\"/>\n" +
                "    </transaction>\n" +
                "\n" +
                "    <transaction id=\"T2\" accessTimeUnit=\"5\">\n" +
                "        <operation var=\"start\"  type=\"reserved\"/>\n" +
                "        <operation var=\"A\"      type=\"read\"/>\n" +
                "        <operation var=\"A\"      type=\"sub\">50</operation>\n" +
                "        <operation var=\"A\"      type=\"write\"/>\n" +
                "        <operation var=\"B\"      type=\"read\"/>\n" +
                "        <operation var=\"B\"      type=\"add\">50</operation>\n" +
                "        <operation var=\"B\"      type=\"write\"/>\n" +
                "        <operation var=\"commit\" type=\"reserved\"/>\n" +
                "    </transaction>\n" +
                "</transactions>");

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
    }
}
