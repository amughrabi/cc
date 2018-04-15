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
<s:form action="perform">
    <s:label for="snapshotXML" value="System Snapshot" cssStyle="font-weight: bold"/>
    <s:textarea name="snapshotXML" cols="60" rows="38" >
        <jsp:attribute name="value">
            <?xml version="1.0" encoding="UTF-8"?>
<snapshot>
    <!--
    Read(A)
    A = A - 50
    Write(A)
    Read(B)
    B = B+50
    Write(B)
    -->
    <disk>
        <block id="A">10</block>
        <block id="B">9</block>
    </disk>

    <transaction id="T1" accessTimeUnit="10">
        <operation var="start"   type="start"/>
        <operation var="A"       type="read"/>
        <operation var="A"       type="sub">50</operation>
        <operation var="A"       type="write"/>
        <operation var="failure" type="failure"/>
        <operation var="B"       type="read"/>
        <operation var="B"       type="add">50</operation>
        <operation var="B"       type="write"/>
        <operation var="commit"  type="commit"/>
    </transaction>

    <transaction id="T2" accessTimeUnit="5">
        <operation var="start"   type="start"/>
        <operation var="A"       type="read"/>
        <operation var="A"       type="sub">50</operation>
        <operation var="A"       type="write"/>
        <operation var="B"       type="read"/>
        <operation var="B"       type="add">50</operation>
        <operation var="B"       type="write"/>
        <operation var="commit"  type="commit"/>
    </transaction>
</snapshot>
        </jsp:attribute>
    </s:textarea>
    <s:label for="recoveryMethod" value="Recovery Method" cssStyle="font-weight: bold"/>
    <s:radio list="#{'deferred' : 'Deferred', 'immediate' : 'Immediate'}" name="recoveryMethod" value="'deferred'" />
    <s:submit value="Run"/>
</s:form>

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

    <h3>Buffer</h3>
    <table style="border: 1px solid #000">
        <c:forEach items="${snapshotAfter.buffer.blocks}" var="block">
            <tr>
                <td>${block.id}</td>
                <td>${block.value}</td>
            </tr>
        </c:forEach>
    </table>
    <h3>Disk (After execution)</h3>
    <table style="border: 1px solid #000">
        <c:forEach items="${snapshotAfter.disk.blocks}" var="block">
            <tr>
                <td>${block.id}</td>
                <td>${block.value}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>
