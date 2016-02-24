<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<%@page import="java.time.ZoneId"%>
<%@page import="java.time.ZonedDateTime"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<h1><s:message code="headline" /></h1>
	<s:message code="servertime" />: <%=ZonedDateTime.now(ZoneId.of("UTC"))%>
</body>
</html>