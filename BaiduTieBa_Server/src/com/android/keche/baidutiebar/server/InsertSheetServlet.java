package com.android.keche.baidutiebar.server;

import java.io.IOException;
import java.io.PrintWriter;
//import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.jasper.tagplugins.jstl.core.Out;

import com.android.keche.baidutiebar.server.bean.FirstPageBean;
import com.android.keche.baidutiebar.server.util.DbUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class InsertSheetServlet
 */
@WebServlet("/InsertSheetServlet")
public class InsertSheetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertSheetServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String sheetJson = request.getParameter("sheet_json");
		if (sheetJson == null || sheetJson.equals("")) {
			System.err.println("接收json字符串为空！");
			return;
		}
		FirstPageBean pageBean = new Gson().fromJson(sheetJson, FirstPageBean.class);
		String iconURL = pageBean.getUserIconURL();
		if (iconURL != null && !iconURL.equals("")) {
			int start = iconURL.lastIndexOf("\\");
			if (start <= 0) {
				start = iconURL.lastIndexOf("/");
			}
			pageBean.setUserIconURL("userIcon/" + iconURL.substring(start + 1));
			System.out.println(pageBean.getUserIconURL());
		}
		DbUtil.openQueryConn();
		ResultSet userResult = DbUtil.query("user", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "name", "=", "'" + pageBean.getUserName() + "'", "")
		});
		ResultSet barResult = DbUtil.query("bar", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "name", "=", "'" + pageBean.getSource() + "'", "")
		});
		int barId = 0;
		int hostId = 0;
		try {
			if (userResult != null && userResult.first()) {
				hostId = userResult.getInt("id");
			}
			if (barResult != null && barResult.first()) {
				barId = barResult.getInt("id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DbUtil.closeQueryConn();
		DbUtil.insert("sheet", 
				new String[] {"user_name", "user_icon", "dist_date",
				"dist_title", "dist_content", "share_num", "comment_num",
				"like_num", "bar_id", "host_id"}, 
				new String[]{stringLizeForSQL(pageBean.getUserName()), stringLizeForSQL(pageBean.getUserIconURL()), stringLizeForSQL(pageBean.getDate()), 
						stringLizeForSQL(pageBean.getTitle()), stringLizeForSQL(pageBean.getContent()), String.valueOf(pageBean.getShareNum()),
						String.valueOf(pageBean.getCommentNum()), String.valueOf(pageBean.getLikeNum()),
						String.valueOf(barId), String.valueOf(hostId)});
		DbUtil.openQueryConn();
		ResultSet sheetSet = DbUtil.query("sheet", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "dist_content", "=", stringLizeForSQL(pageBean.getContent()), ""),
				new DbUtil.QuerySet("AND", "dist_date", "=", stringLizeForSQL(pageBean.getDate()), "")
		});
		try {
			if (sheetSet != null) {
				if (sheetSet.first()) {
					if (pageBean.getImageURLs() != null) {
						for (String imageURL: pageBean.getImageURLs()) {
							if (imageURL != null && !imageURL.equals("")) {
								int start = imageURL.lastIndexOf("\\");
								if (start <= 0) {
									start = imageURL.lastIndexOf("/");
								}
								imageURL = "sheetImage/" + imageURL.substring(start + 1);
								DbUtil.insert("sheet_image", new String[] {"sheet_id", "image_url"}, 
										new String[] {
												String.valueOf(sheetSet.getInt("id")),
												stringLizeForSQL(imageURL)
												});
							}
						}
					} else {
						System.err.println("请检查刚插入的帖子记录！");
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sheetJson);
		System.out.println("插入成功！");
		PrintWriter out = response.getWriter();
		out.println("解析成功！");
	}
	
	private String stringLizeForSQL(String string) {
		return "'" + string + "'";
	}
}
