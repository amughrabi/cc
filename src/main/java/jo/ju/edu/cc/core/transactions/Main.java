package jo.ju.edu.cc.core.transactions;

import java.util.List;

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
                "    <transaction id=\"T1\" accessTimestamp=\"1523059200\">\n" +
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
                "    <transaction id=\"T2\" accessTimestamp=\"1523104200\">\n" +
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

        for(Block block : snapshot.getBuffer().getBlocks()) {
            System.out.println(block);
        }

        System.out.println(snapshot.getBuffer());
    }
}
