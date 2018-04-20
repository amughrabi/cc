<%@ page import="jo.ju.edu.cc.core.util.StringUtil" %>
<%@ page import="jo.ju.edu.cc.core.recovery.LogBasedRecovery" %>
<%@ page import="jo.ju.edu.cc.core.recovery.LogEntry" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="jo.ju.edu.cc.core.transactions.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="snapshot" type="jo.ju.edu.cc.core.transactions.Snapshot"--%>
<%--@elvariable id="snapshotAfter" type="jo.ju.edu.cc.core.transactions.Snapshot"--%>
<%--@elvariable id="timeFrameTable" type="jo.ju.edu.cc.core.transactions.TimeFrameTable"--%>
<%--@elvariable id="operation" type="jo.ju.edu.cc.core.transactions.Operation"--%>
<html>
<head>
    <title>Concurrency Control Techniques</title>
</head>
<body>
<c:if test="${not empty error}">
    <h2 style="color:red">${error}</h2>
</c:if>
<s:form action="perform" theme="simple">
    <a href="https://github.com/amughrabi/cc">How to write the System XML?</a>
    <h3>System Snapshot</h3>
    <%
        String snapshotXML = (String) request.getAttribute("snapshotXML");
        if(StringUtil.isNullOrEmptyOrWhiteSpace(snapshotXML)) {
            snapshotXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                    "</snapshot>";
        }
        pageContext.setAttribute("snapshotXML", snapshotXML);
    %>
    <s:label for="snapshotXML" value="System Snapshot" cssStyle="font-weight: bold; display: none;"/>
    <s:textarea name="snapshotXML" cols="60" rows="38" >
        <jsp:attribute name="value">${snapshotXML}</jsp:attribute>
    </s:textarea>
    <%
        String rMethod = (String) request.getAttribute("recoveryMethod");
        if(StringUtil.isNullOrEmptyOrWhiteSpace(rMethod)) {
            rMethod = "logBasedDeferred";
        }
        pageContext.setAttribute("rMethod", rMethod);

        String cct = (String) request.getAttribute("useStrict2PL");
        if(StringUtil.isEqual(cct, "true")) {
            pageContext.setAttribute("useStrict2PL", "checked");
        }
    %>
    <h3>Recovery Method</h3>
    <label for="logBasedDeferred">
        <input type="radio" name="recoveryMethod" value="logBasedDeferred" id="logBasedDeferred" ${empty rMethod or rMethod == 'logBasedDeferred' ? 'checked' : ''}/><span>Deferred</span>
    </label>
    <label for="logBasedImmediate">
        <input type="radio" name="recoveryMethod" value="logBasedImmediate" id="logBasedImmediate" ${rMethod == 'logBasedImmediate' ? 'checked' : ''}/><span>Immediate</span>
    </label>
    <h3>Concurrency Control Techniques</h3>
    <label for="useStrict2PL">
        <input type="checkbox" id="useStrict2PL" name="useStrict2PL" value="true" ${useStrict2PL} > <span>Strict 2PL</span>
    </label>

    <br/> <br/>
    <s:submit value="Run"/>
</s:form>

<%
    // I would like to enforce the MVC, but the code will get more complicated. I totally agree to be simple with
    // some bad practices over than best practice with complicated code.

    String snapshotXML = (String) request.getAttribute("snapshotXML");
    if(StringUtil.hasText(snapshotXML)) {
        Snapshot snapshot = TransactionsManager.load(snapshotXML);

        TimeFrameTable timeFrameTable = TransactionsManager.constructTimeFrameTable(snapshot);

        pageContext.setAttribute("timeFrameTable", timeFrameTable);
        pageContext.setAttribute("snapshot", snapshot);
%>

<c:if test="${not empty snapshot}">
    <h3>Disk (Before execution)</h3>
    <table style="border: 1px solid #000">
        <c:forEach items="${snapshot.disk.blocks}" var="block">
            <tr>
                <td>${block.id}</td>
                <td>${block.value}</td>
            </tr>
        </c:forEach>
    </table>

    <h3>Time frame Table</h3>
    <%--
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
    --%>
    <table border="1">
        <c:forEach items="${timeFrameTable.table.keySet()}" var="tUnit">
            <tr>
                <td>${tUnit}</td>
                <c:forEach var="operation" items="${timeFrameTable.table.get(tUnit)}">
                    <td>
                        <c:if test="${not (operation.isNull())}">
                            &lt;${operation.transactionId}, <%--
                            --%><c:choose><%--
                            --%><c:when test="${operation.type == 'start' || operation.type == 'commit'}"><%--
                            --%><strong>${operation.type}</strong><%--
                            --%></c:when><%--
                            --%><c:otherwise><%--
                            --%><c:choose><%--
                            --%><c:when test="${operation.variable == 'failure'}"><span style="color: red; font-weight: bold;">${operation.variable}</span></c:when><c:otherwise>${operation.variable}</c:otherwise></c:choose><%--
                            --%><c:if test="${not (operation.type == 'failure')}">, ${operation.type}</c:if><c:if test="${operation.type == 'add' || operation.type == 'sub' ||operation.type == 'multi' || operation.type == 'divide'}">, ${operation.value}</c:if><%--
                            --%></c:otherwise><%--
                            --%></c:choose>&gt;</td>
                        </c:if>
                </c:forEach>
                <c:if test="${timeFrameTable.table.get(tUnit).size() < timeFrameTable.maxListSize}">
                    <c:forEach begin="1" end="${timeFrameTable.maxListSize - timeFrameTable.table.get(tUnit).size()}">
                        <td> </td>
                    </c:forEach>
                </c:if>
            </tr>
        </c:forEach>
    </table>

    <%
        String recoveryMethod = (String)request.getAttribute("recoveryMethod");
        if(StringUtil.hasText(recoveryMethod)) {
            Protocol protocol = Protocol.LOG_BASED_DEFERRED;
            if(StringUtil.isEqual(recoveryMethod, Protocol.LOG_BASED_IMMEDIATE.toString())) {
                protocol = Protocol.LOG_BASED_IMMEDIATE;
            }
            String cct = (String) request.getAttribute("useStrict2PL");
            boolean isStrict2PL = StringUtil.isEqual(cct, "true");
            snapshot = TransactionsManager.execute(snapshot, timeFrameTable, protocol, isStrict2PL);

            pageContext.setAttribute("snapshot", snapshot);
            pageContext.setAttribute("timeFrameTable", snapshot.getTimeFrameTable());
    %>

    <%

        if(isStrict2PL) {
    %>
    <h3>Strict 2PL Time frame Table</h3>
    <table border="1">
        <c:forEach items="${timeFrameTable.table.keySet()}" var="tUnit">
            <tr>
                <td>${tUnit}</td>
                <c:forEach var="operation" items="${timeFrameTable.table.get(tUnit)}">
                    <td><%--
                    --%><c:if test="${not (operation.isNull())}"><%--
                    --%>&lt;${operation.transactionId}, <%--
                        --%><c:choose><%--
                        --%><c:when test="${operation.type == 'start' || operation.type == 'commit' ||  operation.wait}"><%--
                            --%><strong>${operation.type}</strong><%--
                                --%></c:when><%--
                            --%><c:otherwise><%--
                            --%><c:choose><%--
                                --%><c:when test="${operation.variable == 'failure'}"><span style="color: red; font-weight: bold;">${operation.variable}</span></c:when><c:otherwise>${operation.variable}</c:otherwise></c:choose><%--
                                    --%><c:if test="${not (operation.type == 'failure')}">, ${operation.type}</c:if><c:if test="${operation.type == 'add' || operation.type == 'sub' ||operation.type == 'multi' || operation.type == 'divide'}">, ${operation.value}</c:if><%--
                                --%></c:otherwise><%--
                            --%></c:choose>&gt;${operation.commit ? '(<span style="color:red; font-weight:bold;">unlock</span>)' : ''}<%--
                        --%><c:if test="${operation.read or operation.write}"><%--
                        --%><span style="color:red; font-weight:bold;"> lock-${operation.lockType}(${operation.variable})</span><%--
                            --%></c:if><%--
                        --%></td>
                    </c:if>
                </c:forEach>
                <c:if test="${timeFrameTable.table.get(tUnit).size() < timeFrameTable.maxListSize}">
                    <c:forEach begin="1" end="${timeFrameTable.maxListSize - timeFrameTable.table.get(tUnit).size()}">
                        <td> </td>
                    </c:forEach>
                </c:if>
            </tr>
        </c:forEach>
    </table>

    <%

        }
    %>
    <h3>Buffer</h3>
    <table style="border: 1px solid #000">
        <c:forEach items="${snapshot.buffer.blocks}" var="block">
            <tr>
                <td>${block.id}</td>
                <td>${block.value}</td>
            </tr>
        </c:forEach>
    </table>
    <h3>Disk (After execution)</h3>
    <table style="border: 1px solid #000">
        <c:forEach items="${snapshot.disk.blocks}" var="block">
            <tr>
                <td>${block.id}</td>
                <td>${block.value}</td>
            </tr>
        </c:forEach>
    </table>
    <%

            LogBasedRecovery logBasedRecovery = snapshot.getLogBasedRecovery();
            Map<String, List<LogEntry>> logMap = logBasedRecovery.getLog();
            pageContext.setAttribute("logMap", logMap);
    %>

    <h3>Logs</h3>
    <div>
    <%
            for(String transactionId : logMap.keySet()) {
                List<LogEntry> entries = logMap.get(transactionId);
                String txt = "";
                for(LogEntry entry : entries) {
                    txt = ("&lt;" + entry.getTransactionId()) ;
                    if(!StringUtil.isEqual(entry.getVariable(), "start") && !StringUtil.isEqual(entry.getVariable(), "commit")) {
                     txt += ", " + entry.getOldValue() + ", " + entry.getNewValue();
                    } else {
                        txt += ", <strong>" + entry.getVariable() + "</strong>";
                    }
                    txt += "&gt;";

                    out.print(  txt + "<br/>");
                }
            }
    %>
    </div>
    <%
        if(RecoveryManager.requireRecover(snapshot)) {
            snapshot = RecoveryManager.recover(snapshot);
            pageContext.setAttribute("snapshot", snapshot);
    %>
    <h3>Disk (Recovered)</h3>
    <table style="border: 1px solid #000">
        <c:forEach items="${snapshot.disk.blocks}" var="block">
            <tr>
                <td>${block.id}</td>
                <td>${block.value}</td>
            </tr>
        </c:forEach>
    </table>

    <%
        } else {
            out.print("<h3 style='color:green'>Hurrah! - No recovery required! :) The database now is consistent!!</h3>");
        }
    %>
    <%
        }
    %>
</c:if>
<%
    }
%>
</body>
</html>
