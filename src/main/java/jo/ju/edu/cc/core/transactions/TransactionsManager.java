package jo.ju.edu.cc.core.transactions;

import jo.ju.edu.cc.core.recovery.LogBasedRecovery;
import jo.ju.edu.cc.core.xml.XMLParser;
import jo.ju.edu.cc.core.xml.XMLParserImpl;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransactionsManager {

    public static @NotNull Snapshot load(@NotNull String xml) throws Exception {
        XMLParser parser = new XMLParserImpl();
        Document document = parser.parse(xml);
        List<Transaction> transactions = new ArrayList<>();
        NodeList root = document.getChildNodes();
        Node rootNode;
        NodeList children;
        Node node;
        NodeList operationsNode;
        Node operationNode;
        NamedNodeMap opsAttributes;
        Operation operation;
        List<Operation> operations;
        Transaction transaction;
        NamedNodeMap transAttributes;
        Buffer buffer = new Buffer();
        List<Block> blocks;
        NodeList blocksNode;
        Node blockNode;
        Block block;
        NamedNodeMap blockNodeAttrs;
        Node blockNodeAttr;
        for(int rootIdx = 0; rootIdx < root.getLength(); rootIdx++) {
            rootNode = root.item(rootIdx);
            if(rootNode.getNodeType() == Node.ELEMENT_NODE && rootNode.hasChildNodes()) {
                children = rootNode.getChildNodes();
                for(int i = 0; i < children.getLength(); i++) {
                    node = children.item(i);
                    if(node.getNodeType() == Node.ELEMENT_NODE && node.hasChildNodes()) {
                        if(node.getNodeName().equals("transaction")) {
                            transaction = new Transaction();
                            if(node.hasAttributes()) {
                                transAttributes = node.getAttributes();
                                for(int tIdx = 0; tIdx < transAttributes.getLength(); tIdx ++) {
                                    Node attr = transAttributes.item(tIdx);
                                    transaction.put(attr.getNodeName(), attr.getNodeValue());
                                }
                            }
                            operations = new ArrayList<>();
                            operationsNode = node.getChildNodes();
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
                        } else { // buffer
                            blocks = new ArrayList<>();
                            blocksNode = node.getChildNodes();
                            for(int b = 0; b < blocksNode.getLength(); b++) {
                                blockNode = blocksNode.item(b);
                                if(blockNode.getNodeType() == Node.ELEMENT_NODE && blockNode.hasAttributes()) {
                                    blockNodeAttrs = blockNode.getAttributes();
                                    block = new Block();
                                    for(int bkIdx = 0; bkIdx < blockNodeAttrs.getLength(); bkIdx++) {
                                        blockNodeAttr = blockNodeAttrs.item(bkIdx);
                                        block.put(blockNodeAttr.getNodeName(), blockNodeAttr.getNodeValue());
                                    }
                                    block.setValue(blockNode.getTextContent());
                                    blocks.add(block);
                                }
                            }
                            buffer.setBlocks(blocks);
                        }
                    }
                }
            }
        }

        return new Snapshot(buffer, transactions);
    }

    public static void execute(@NotNull Snapshot snapshot, @NotNull Protocol protocol) {
        List<Transaction> transactions = snapshot.getTransactions();
        Buffer buffer = snapshot.getBuffer();
        // sort them on AccessTimestamp
        transactions.sort(Comparator.comparingLong(Transaction::getAccessTimestamp));
        // log based
        LogBasedRecovery logger = new LogBasedRecovery();
        List<Operation> operations;
        // We need to build a time frame table.
        for(Transaction transaction : transactions) {
            operations = transaction.getOperations();
        }

        switch (protocol) {
            case LOG_BASED_DEFERRED:

                break;
        }
    }
}

