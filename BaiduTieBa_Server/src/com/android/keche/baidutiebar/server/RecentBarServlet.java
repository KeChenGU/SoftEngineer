package com.android.keche.baidutiebar.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.keche.baidutiebar.server.bean.RecentBarBean;
import com.android.keche.baidutiebar.server.util.DbUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class RecentBarServlet
 */
@WebServlet("/RecentBarServlet")
public class RecentBarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecentBarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String userId = request.getParameter("user_id");
		System.out.println("userId:" + userId);
		DbUtil.openQueryConn();
		ResultSet userResult = DbUtil.query("user", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "id", "=", userId, "")
		});
		ResultSet barResult = DbUtil.query("bar_recent", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "user_id", "=", userId, ""),
				new DbUtil.QuerySet("AND", "bar_id", "<>", "0", "")
		});
		List<RecentBarBean> recentBars = new ArrayList<>();
		try {
			if (userResult != null && userResult.first()) {
				if (barResult != null) {
					barResult.beforeFirst();
					while (barResult.next()) {
						RecentBarBean recentBar = new RecentBarBean();
						String barId = barResult.getString("bar_id");
						ResultSet bar = DbUtil.query("bar", new DbUtil.QuerySet[] {
								new DbUtil.QuerySet("", "id", "=", barId, ""),
						});
						recentBar.setBarName(bar.getString("name"));
						recentBar.setIconUrl(bar.getString("icon"));
						recentBars.add(recentBar);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("RecentBarBean:" + e.getMessage());
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(recentBars));
		DbUtil.closeQueryConn();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
