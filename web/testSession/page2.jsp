<%--
  Created by IntelliJ IDEA.
  User: dropa
  Date: 2022/11/10
  Time: 00:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div>page2</div>
    <%
        HttpSession hs=request.getSession();
        if (hs.isNew()) out.println("新session");
        out.println(hs.getAttribute("0"));
    %>
</body>
</html>
