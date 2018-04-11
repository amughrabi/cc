package jo.ju.edu.cc.core.transactions;

import jo.ju.edu.cc.core.recovery.ILogEntry;
import jo.ju.edu.cc.core.recovery.LogBasedRecovery;
import jo.ju.edu.cc.core.recovery.LogEntry;
import jo.ju.edu.cc.core.util.StringUtil;
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
import java.util.Map;

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
        Disk disk = new Disk();
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
                                    operation.setTransactionId(transaction.getId());
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
                        } else { // disk
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
                            disk.setBlocks(blocks);
                        }
                    }
                }
            }
        }

        return new Snapshot(disk, transactions);
    }

    public static @NotNull TimeFrameTable constructTimeFrameTable(@NotNull Snapshot snapshot) {
        List<Transaction> transactions = snapshot.getTransactions();
        // sort them on AccessTimestamp
        transactions.sort(Comparator.comparingLong(Transaction::getAccessTimeUnit));
        List<Operation> operations;
        // We need to build a time frame table.
        TimeFrameTable timeFrameTable = new TimeFrameTable();
        long timeUnit;
        Transaction transaction;
        for(int idx = 0; idx < transactions.size(); idx++) {
            transaction = transactions.get(idx);
            timeUnit = transaction.getAccessTimeUnit();
            operations = transaction.getOperations();
            for(Operation operation : operations) {
                timeFrameTable.put(timeUnit, operation, idx);
                timeUnit++;
            }
        }
        return timeFrameTable;
    }

    public static void execute(@NotNull Snapshot snapshot, @NotNull TimeFrameTable timeFrameTable, @NotNull Protocol protocol) {
        // Disk
        Disk disk = snapshot.getDisk();
        Map<Long, List<Operation>> table = timeFrameTable.getTable();
        // Create an empty buffer
        Buffer buffer = new Buffer();
        // Define CPU Registers
        Registers registers = new Registers();
        // Create the log file
        LogBasedRecovery log = new LogBasedRecovery();
        for(long timeUnit : timeFrameTable.getTable().keySet()) {
            // all operation need to be executed. If the protocol is deferred, the write will be in buffer until
            // the transaction COMMIT.
            List<Operation> operations = table.get(timeUnit);
            for(Operation operation : operations) {


                if( StringUtil.isEqual(operation.getType(), IOperation.START) ) {
                    // <transactionId, variable, oldValue, newValue>
                    // <T0, X, 50, 100>
                    LogEntry logEntry = new LogEntry();
                    logEntry.put(ILogEntry.transactionId, operation.getTransactionId());
                    logEntry.put(ILogEntry.variable, operation.getVariable());
                    logEntry.put(ILogEntry.oldValue, "");
                    logEntry.put(ILogEntry.newValue, operation.getValue());

                    log.log(operation.getTransactionId(), logEntry);
                } else if (StringUtil.isEqual(operation.getType(), IOperation.READ)) {
                    // copy the variable from disk to buffer.
                    Block block = disk.getBlock(operation.getVariable());
                    if(block != null) {
                        buffer.addBlock( block );
                        // copy the variable from buffer to transaction register
                        registers.addOrUpdateBlock(operation.getTransactionId(), block);
                    }
                } else if (StringUtil.isEqual(operation.getType(), IOperation.WRITE)) {
                    // write the data from registers to buffer
                    Block rblock = registers.getBlock(operation.getTransactionId(), operation.getVariable());
                    if(rblock != null) {
                        buffer.addBlock(rblock);
                        // In case of immediate update
                        if(protocol == Protocol.LOG_BASED_IMMEDIATE) {
                            Block block = buffer.getBlock(operation.getVariable());
                            if(block != null) {
                                disk.addBlock(block);
                            }
                        }
                    }
                } else if (StringUtil.isEqual(operation.getType(), IOperation.COMMIT)) {

                }
            }
        }
    }
}

