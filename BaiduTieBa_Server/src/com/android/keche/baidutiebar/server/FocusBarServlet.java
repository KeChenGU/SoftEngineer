package com.android.keche.baidutiebar.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.keche.baidutiebar.server.bean.FocusBean;
//import com.android.keche.baidutiebar.server.bean.RecentBarBean;
import com.android.keche.baidutiebar.server.util.DbUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class FocusBarServlet
 */
@WebServlet("/FocusBarServlet")
public class FocusBarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FocusBarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String userId = request.getParameter("user_id");
		System.out.println("userId:" + userId);
		DbUtil.openQueryConn();
		ResultSet userResult = DbUtil.query("user", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "id", "=", userId, "")
		});
		ResultSet barResult = DbUtil.query("bar_focus", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "user_id", "=", userId, ""),
				new DbUtil.QuerySet("AND", "bar_id", "<>", "0", "")
		});
		FocusBean focusBar = new FocusBean();
		try {
			if (userResult != null && userResult.first()) {
				if (barResult != null) {
					barResult.beforeFirst();
					while (barResult.next()) {
						String barId = barResult.getString("bar_id");
						ResultSet bar = DbUtil.query("bar", new DbUtil.QuerySet[] {
								new DbUtil.QuerySet("", "id", "=", barId, ""),
						});
						focusBar.getMyFocuText().add((bar.getString("name")));
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("FocusBar:" + e.getMessage());
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(focusBar));
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
