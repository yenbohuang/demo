<%@page import="java.time.ZoneId"%>
<%@page import="java.time.ZonedDateTime"%>

<html><body>
<h1>JSP demo</h1>
Server Time: <%=ZonedDateTime.now(ZoneId.of("UTC"))%>
</body></html>