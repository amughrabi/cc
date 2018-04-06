package jo.ju.edu.cc.core.transactions;

import jo.ju.edu.cc.core.xml.XMLParser;
import jo.ju.edu.cc.core.xml.XMLParserImpl;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.util.ArrayList;
import java.util.List;

public class TransactionsManager {

    public static @NotNull List<Transaction> load(@NotNull String xml) throws Exception {
        XMLParser parser = new XMLParserImpl();
        Document document = parser.parse(xml);
        List<Transaction> transactions = new ArrayList<>();
        NodeList root = document.getChildNodes();
        Node rootNode;
        NodeList children;
        Node transactionNode;
        NodeList operationsNode;
        Node operationNode;
        NamedNodeMap opsAttributes;
        Operation operation;
        List<Operation> operations;
        Transaction transaction;
        NamedNodeMap transAttributes;
        for(int rootIdx = 0; rootIdx < root.getLength(); rootIdx++) {
            rootNode = root.item(rootIdx);
            if(rootNode.getNodeType() == Node.ELEMENT_NODE && rootNode.hasChildNodes()) {
                children = rootNode.getChildNodes();
                for(int i = 0; i < children.getLength(); i++) {
                    transactionNode = children.item(i);
                    if(transactionNode.getNodeType() == Node.ELEMENT_NODE && transactionNode.hasChildNodes()) {
                        transaction = new Transaction();
                        if(transactionNode.hasAttributes()) {
                            transAttributes = transactionNode.getAttributes();
                            for(int tIdx = 0; tIdx < transAttributes.getLength(); tIdx ++) {
                                Node attr = transAttributes.item(tIdx);
                                transaction.put(attr.getNodeName(), attr.getNodeValue());
                            }
                        }
                        operations = new ArrayList<>();
                        operationsNode = transactionNode.getChildNodes();
                        for(int idx = 0; idx < operationsNode.getLength(); idx++) {
                            operationNode = operationsNode.item(idx);
                            if(operationNode.getNodeType() == Node.ELEMENT_NODE && operationNode.hasAttributes()) {
                                opsAttributes = operationNode.getAttributes();
                                operation = new Operation();
                                for(int optIdx = 0; optIdx < opsAttributes.getLength(); optIdx++) {
                                    Node attr = opsAttributes.item(optIdx);
                                    operation.put(attr.getNodeName(), attr.getNodeValue());
                                }
                                operation.setValue(operationNode.getTextContent());
                                operations.add(operation);
                            }
                        }
                        transaction.setOperations(operations);
                        transactions.add(transaction);
                    }
                }
            }
        }

        return transactions;
    }
}

class Main {
    public static void main(String... args) throws Exception {

       List<Transaction> transactions = TransactionsManager.load("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<transactions>\n" +
                "    <!--\n" +
                "    Read(A)\n" +
                "    A = A - 50\n" +
                "    Write(A)\n" +
                "    Read(B)\n" +
                "    B = B+50\n" +
                "    Write(B)\n" +
                "    -->\n" +
                "    <transaction id=\"T1\" accessTimestamp=\"1523059200\">\n" +
                "        <operation var=\"A\" type=\"read\"/>\n" +
                "        <operation var=\"A\" type=\"sub\">50</operation>\n" +
                "        <operation var=\"A\" type=\"write\"/>\n" +
                "        <operation var=\"B\" type=\"read\"/>\n" +
                "        <operation var=\"B\" type=\"add\">50</operation>\n" +
                "        <operation var=\"B\" type=\"write\"/>\n" +
                "    </transaction>\n" +
                "    \n" +
                "    <transaction id=\"T2\" accessTimestamp=\"1523104200\">\n" +
                "        <operation var=\"A\" type=\"read\"/>\n" +
                "        <operation var=\"A\" type=\"sub\">50</operation>\n" +
                "        <operation var=\"A\" type=\"write\"/>\n" +
                "        <operation var=\"B\" type=\"read\"/>\n" +
                "        <operation var=\"B\" type=\"add\">50</operation>\n" +
                "        <operation var=\"B\" type=\"write\"/>\n" +
                "    </transaction>\n" +
                "</transactions>");

      for(Transaction transaction: transactions) {
          System.out.println(transaction);
          //System.out.println(transaction);
      }
    }
}
