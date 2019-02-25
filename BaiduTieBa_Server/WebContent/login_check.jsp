<%@ page import="com.android.keche.baidutiebar.server.util.DbUtil,java.sql.*,java.util.*" %>
<%@ page import="com.android.keche.baidutiebar.server.bean.UserExBean" %>
<%@ page import="org.json.*"%>
<%@ page import="com.google.gson.*" %>
<%@ page language="java" contentType="text/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
String createJsonObjectString(String[] attributes, String[] values) {
	StringBuilder builder = new StringBuilder();
	int i = 0;
	builder.append("{");
	for (String attr: attributes) {
		builder.append("\'" + attr + "\'").append(":").append("\'" + values[i++] + "\'");
		if (!attr.equals(attributes[attributes.length - 1])) {
			builder.append(",");
		}
	}
	builder.append("}");
	return builder.toString();
}
String createJsonArrayString(List<String> jsonObjectStrings) {
	StringBuilder builder = new StringBuilder();
	builder.append("[");
	int length = jsonObjectStrings.size();
	for (int i = 0; i < length; ++i) {
		builder.append(jsonObjectStrings.get(i));
		if (i != length - 1) {
			builder.append(",");
		}
	}
	builder.append("]");
	return builder.toString();
}
%>
<% 
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

String userName = request.getParameter("name");
String password = request.getParameter("password");
/* boolean exist = DbUtil.boolQuery("bar_user", new DbUtil.QuerySet[] {
		new DbUtil.QuerySet("", "name", "=", userName, " "),
		new DbUtil.QuerySet("AND ", "password", "=", password, ""),
}); */
/* if (userName == null) {
	userName = "18906632597";
}
if (password == null) {
	password = "123456";
} */
DbUtil.openQueryConn();
ResultSet resultSet = DbUtil.query("user", new DbUtil.QuerySet[] {
		new DbUtil.QuerySet("", "name", "=", "'" + userName + "'", " "),
		new DbUtil.QuerySet("AND ", "password", "=", "'" + password + "'", "")
});
if (resultSet == null) {
	out.print("用户不存在！请先注册账户！");
	return;
}
//resultSet.beforeFirst();
resultSet.first();
UserExBean user = new UserExBean(resultSet.getString("name"), resultSet.getString("password"));
user.setId(resultSet.getInt("id"));
user.setOld(resultSet.getString("old"));
user.setIconUrl(resultSet.getString("icon_url"));
user.setFansNum(resultSet.getInt("fans_num"));
user.setFocusNum(resultSet.getInt("focus_num"));
Gson gson = new Gson();
System.out.println(gson.toJson(user));
out.print(gson.toJson(user));

%>
