<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<link href="lib/bootstrap-3.3.6/css/bootstrap.min.css" rel="stylesheet">
	<script src="lib/jquery/jquery-2.2.1.min.js"></script>
	<script src="lib/bootstrap-3.3.6/js/bootstrap.min.js"></script>
	
	<script type="text/javascript">
	var json = "{\"states\":[{\"date\":\"2016/11/22\", \"time\":\"11:20\"},{\"date\":\"2016/11/23\", \"time\":\"12:00\"}]}";
	
	function clickMe() {
	
		var jsonObj = jQuery.parseJSON(json);
		
		<%--add new element as the first item--%>
		jsonObj.states.unshift(jQuery.parseJSON("{\"date\":\"2016/11/21\", \"time\":\"11:20\"}"));
		
		var dataTable = $("#dataTable"); 		
		dataTable.empty();
		
		dataTable.append("<tr>" +
				"<th>Column 1</th>" +
				"<th>Column 2</th>" +
				"</tr>");
		
		for (i = 0; i < jsonObj.states.length; i++) {
			
			var trClass = "";
			if (i == 0) {
				trClass = " class=\"success\"";
			}
			
			dataTable.append("<tr" + trClass + ">" +
					"<td>date</td>" +
					"<td>" + jsonObj.states[i].date + "</td>" +
					"</tr>");
			
			dataTable.append("<tr" + trClass + ">" +
					"<td>time</td>" +
					"<td>" + jsonObj.states[i].time + "</td>" +
					"</tr>");
		}
	}
	</script>
	
</head>
<body>
	 <button type="button" onclick="clickMe()">Click Me!</button> 

	<table id="dataTable" class="table table-hover">
		<tr>
			<td>Init 1</td>
			<td>Init 2</td>
		</tr>
	</table>
</body>
</html>