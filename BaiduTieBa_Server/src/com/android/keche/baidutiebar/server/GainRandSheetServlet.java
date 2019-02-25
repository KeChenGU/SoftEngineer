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

import com.android.keche.baidutiebar.server.bean.FirstPageBean;
import com.android.keche.baidutiebar.server.util.DbUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class GainRandSheetServlet
 */
@WebServlet("/GainRandSheetServlet")
public class GainRandSheetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GainRandSheetServlet() {
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
		ResultSet result;
		DbUtil.openQueryConn();
		result = DbUtil.query("sheet", new DbUtil.QuerySet[] {
				new DbUtil.QuerySet("", "id", "<", "100", "")
		});
		List<FirstPageBean> firstPageBeans = new ArrayList<>();
		try {
			while (result.next()) {
				FirstPageBean firstPage = new FirstPageBean();
				firstPage.setTitle(result.getString("dist_title"));
				firstPage.setUserName(result.getString("user_name"));
				firstPage.setUserIconURL(result.getString("user_icon"));
				firstPage.setDate(result.getString("dist_date"));
				firstPage.setContent(result.getString("dist_content"));
				firstPage.setShareNum(result.getInt("share_num"));
				firstPage.setCommentNum(result.getInt("comment_num"));
				firstPage.setLikeNum(result.getInt("like_num"));
				List<String> imageURLs = new ArrayList<>();
				ResultSet images = DbUtil.query("sheet_image", new DbUtil.QuerySet[] {
						new DbUtil.QuerySet("", "sheet_id", "=",  String.valueOf(result.getInt("id")), ""),
						new DbUtil.QuerySet("AND", "sheet_id", "<>", "0", "")
				});
				if (images != null) {
					images.beforeFirst();
					System.out.println("开始获取图片路径！");
					while (images.next()) {
						imageURLs.add(images.getString("image_url"));
						System.out.println(images.getString("image_url"));
					}
				} else {
					System.out.println("图片列表为空！！！");
				}
				firstPage.setImageURLs(imageURLs);
				firstPageBeans.add(firstPage);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		String json = new Gson().toJson(firstPageBeans);
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
