package com.android.keche.baidutiebar.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {

	private static Connection queryConn;
	

	public static void openQueryConn() {
		queryConn = DbUtil.getConnection();
	}
	
	public static void closeQueryConn() {
		if (queryConn != null) {
			try {
				queryConn.close();
			} catch (SQLException exp) {
				exp.printStackTrace();
			}
		}
	}
	
	public static class QuerySet{
		
		String before;
		
		String column;

		String condition;
		
		String value;
		
		String after;

		public QuerySet() {
			//super();
			before = "";
			column = "";
			condition = "";
			value = "";
			after = "";
		}

		public QuerySet(String before, String column, String condition, String value, String after) {
			//super();
			this.before = before;
			this.column = column;
			this.condition = condition;
			this.value = value;
			this.after = after;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return before + " " + column + " " + condition + " " + value + " " + after;//super.toString();
		}

		public String getBefore() {
			return before;
		}

		public void setBefore(String before) {
			this.before = before;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getCondition() {
			return condition;
		}

		public void setCondition(String condition) {
			this.condition = condition;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getAfter() {
			return after;
		}

		public void setAfter(String after) {
			this.after = after;
		}

		
	}
	
	public static Connection getConnection() {
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/baidu_tie_bar";
		String conditions = "?useUnicode=true&characterEncoding=UTF-8&useSSL=true"; //useUnicode放置中文乱码缺省值为false 设置为true后可设置UTF-8字符集
		String user = "root";
		String password = "gkc.666";
		try {
			//加载驱动 反射方式
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url + conditions, user, password);
		} catch(ClassNotFoundException exp) {
			exp.printStackTrace();
			System.out.println("驱动加载失败！");
		} catch(SQLException exp) {
			exp.printStackTrace();
			System.out.println("数据库连接失败！");
		} 
		return conn;
		
	}
	
	public static boolean insert(String table, String[] columns, String[] values) {
		Connection conn = getConnection();
		try {
			String sql = "INSERT INTO " + table;
			int len = columns.length;
			sql += "(";
			for (int i = 0; i < len; ++i) {
				sql += columns[i];
				sql += (i == len - 1) ? "" : ", "; 
			}
			sql += ")";
			sql += " VALUES(";
			for (int i = 0; i < len; ++i) {
				sql += values[i];
				sql += (i == len - 1) ? "" : ", "; 
			}
			sql += ");";
			System.out.printf("sql = %s\n", sql);
			Statement statement = conn.createStatement();
			int count = statement.executeUpdate(sql);
			if (count > 0) {
				System.out.println("插入成功!");
			} else {
				System.out.println("插入失败!");
			}
			//conn.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
			System.out.println("插入异常!");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException exp) {
					exp.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public static boolean boolQuery(String table, QuerySet[] querySet) {
		Connection conn = getConnection();
		try {
			String sql = "SELECT * FROM " + table;
			sql += " WHERE ";
			int len = querySet.length;
			for (int i = 0; i < len; ++i) {
				sql += querySet[i].before + " ";
				sql += querySet[i].column + " ";
				sql += querySet[i].condition + " ";
				sql += querySet[i].value + " ";
				sql += querySet[i].after + " ";
			}
			sql += ";";
			System.out.println(sql);
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return resultSet.first();
		} catch (SQLException exp) {
			exp.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException exp) {
					exp.printStackTrace();
				}
			}
		}
		//return true;
	}
	
	public static ResultSet query(String table, QuerySet[] querySet) {
		Connection conn = queryConn;//getConnection();
		try {
			String sql = "SELECT * FROM " + table;
			sql += " WHERE ";
			int len = querySet.length;
			for (int i = 0; i < len; ++i) {
				sql += querySet[i].before + " ";
				sql += querySet[i].column + " ";
				sql += querySet[i].condition + " ";
				sql += querySet[i].value + " ";
				sql += querySet[i].after + " ";
			}
			sql += ";";
			System.out.println(sql);
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return resultSet.first() ? resultSet : null;
		} catch (SQLException exp) {
			exp.printStackTrace();
			return null;
		}
//		} finally {
//			if (conn != null) {
//				try {
//					conn.close();
//				} catch (SQLException exp) {
//					exp.printStackTrace();
//				}
//			}
//		}
		//return true;
	}

}
