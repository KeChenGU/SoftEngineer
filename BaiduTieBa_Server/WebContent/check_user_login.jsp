<%@ page import="com.android.keche.baidutiebar.server.util.DbUtil,java.sql.*,java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新用户注册</title>
</head>
<body>
<% 
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

String userName = request.getParameter("name");
String password = request.getParameter("password");
String phone = request.getParameter("phone");
String icon = request.getParameter("icon");
boolean exist = DbUtil.boolQuery("bar_user", new DbUtil.QuerySet[] {
		new DbUtil.QuerySet("", "name", "=", userName, ""),
});
if (exist) {
	out.print("该用户已被注册！");
	return;
}

boolean flag = DbUtil.insert("bar_user", new String[] {"name", "password", "phone", "icon"}, 
		new String[] {"'" + userName + "'", "'" + password + "'", "'" + phone + "'", "'" + icon + "'"});


if (flag) {
	out.println("添加成功！");
} else {
	out.println("添加失败！");
}


%>
</body>
</html>