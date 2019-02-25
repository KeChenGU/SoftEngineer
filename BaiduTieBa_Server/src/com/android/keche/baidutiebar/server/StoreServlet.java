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

//import com.android.keche.baidutiebar.server.bean.HistoryBean;
import com.android.keche.baidutiebar.server.bean.StoreBean;
import com.android.keche.baidutiebar.server.util.DbUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class StoreServlet
 */
@WebServlet("/StoreServlet")
public class StoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StoreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String userId = request.getParameter("user_id");
		System.out.println("userId:" + userId);
		DbUtil.openQueryConn();
		ResultSet store = DbUtil.query("user_store", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "user_id", "=", userId, "")
		});
		List<StoreBean> stores = new ArrayList<>();
		try {
			if (store != null) {
				store.beforeFirst();
				while (store.next()) {
					StoreBean bean = new StoreBean();
					bean.setTitle(store.getString("title"));
					bean.setSource(store.getString("source"));
					bean.setTime(store.getString("time"));
					stores.add(bean);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("historyError:" + e.getMessage());
			e.printStackTrace();
		}
		String json = new Gson().toJson(stores);
		PrintWriter out = response.getWriter();
		out.print(json);
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
