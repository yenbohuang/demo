<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<script src="lib/jquery/jquery-2.2.1.min.js"></script>
	
	<script type="text/javascript">
	
	var url = "ws://localhost:8080/springDemo/time";
	console.log(url);
	var sock = new WebSocket(url);
	
	sock.onopen = function() {
		$("#lines").append("Opening<br />");
		reportTime();
	};
	
	sock.onmessage = function(e) {
		$("#lines").append("Received: " + e.data + "<br />");
		//setTimeout(function(){reportTime()}, 200);
	};
	
	sock.onclose = function() {
		$("#lines").append("Closing<br />");
	}
	
	function reportTime() {
		
		var time = new Date();
		
		$("#lines").append("Send time: " + time + "<br />");
		sock.send(time);
	}
	
	</script>
</head>
<body>
	<div id="lines"></div>
</body>
</html>