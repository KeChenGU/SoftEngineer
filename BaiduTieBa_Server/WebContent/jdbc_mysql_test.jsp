<%@ page import="java.sql.Connection" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.android.keche.baidutiebar.server.util.DbUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>连接测试</title>
</head>
<body>
<%
	
	//PrintWriter out = response.getWriter();  //不用定义 out 已经为Tomcat服务器内置对象
	Connection conn = DbUtil.getConnection();
	if (conn != null) {
		out.print("数据库连接成功！");
		conn.close();
	}
	
%>
</body>
</html>