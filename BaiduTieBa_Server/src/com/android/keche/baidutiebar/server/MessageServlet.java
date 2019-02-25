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

import com.android.keche.baidutiebar.server.bean.ReceiveMsgBean;
import com.android.keche.baidutiebar.server.util.DbUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageServlet() {
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
		
		String recvId = request.getParameter("id");
		if (recvId == null || recvId.equals("")) {
			System.err.println("RecvId 为空！");
			return;
		}
		DbUtil.openQueryConn();
		ResultSet msgSet = DbUtil.query("message", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "recv_id", "=", recvId, ""),
		});
		List<ReceiveMsgBean> receiveMsgs = new ArrayList<>(); 
		try {
			if (msgSet == null) {
				System.out.println("无接收信息！");
				return;
			}
			msgSet.beforeFirst();
			while (msgSet.next()) {
				ResultSet nameSet =  DbUtil.query("user", new DbUtil.QuerySet[] {
						new DbUtil.QuerySet("", "id", "=", msgSet.getString("send_id"), "")
				});
				String name = nameSet.getString("name");
				String iconUrl = nameSet.getString("icon_url");
				name = (name == null || name.equals("")) ? "123456" : name;
				iconUrl = (iconUrl == null || iconUrl.equals("")) ? "userIcon/face_happy.png" : iconUrl;
				ReceiveMsgBean msgBean = new ReceiveMsgBean();
				msgBean.setUserName(name);
				msgBean.setUserIconUrl(iconUrl);
				msgBean.setReceiveDate(msgSet.getString("recv_date"));
				msgBean.setRecentMsg(msgSet.getString("recent_msg"));
				msgBean.setReplyMsg(msgSet.getString("reply_msg"));
				msgBean.setSourceBar(msgSet.getString("source_bar"));
				receiveMsgs.add(msgBean);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		String json = new Gson().toJson(receiveMsgs);
		System.out.println("信息解析成功！");
		PrintWriter out = response.getWriter();
		out.print(json);
		DbUtil.closeQueryConn();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		doGet(request, response);
	}

}
