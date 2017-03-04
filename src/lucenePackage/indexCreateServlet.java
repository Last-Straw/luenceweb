package lucenePackage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class indexCreateServlet extends HttpServlet {

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String docDirectory=request.getParameter("docDirectory");
		String indexDirectory=request.getParameter("indexDirectory");
		System.out.println(docDirectory);
		System.out.println(indexDirectory);
		int fileAmount=0;
		try {
			fileAmount=IndexFiles.startCreate(indexDirectory, docDirectory);//IndexFiles.startCreate开始创建索引
		} catch (Exception e) {//当try语句中出现异常是时，会执行catch中的语句
			// TODO Auto-generated catch block   
			e.printStackTrace();//在命令行打印异常信息在程序中出错的位置及原因
		}
		HttpSession session = request.getSession();//获取session  返回的是httpSession 类型的，从后台servlet到jsp的时候把数据放到session里给传到jsp页面。
		//session.setAttribute("sessionName",Object);
		//用来设置session值的，sessionName是名称，object是你要保存的对象。
		session.setAttribute("msgDocDirecotry", docDirectory);
		session.setAttribute("msgIndexDirecotry", indexDirectory);
		session.setAttribute("fileAmount", ""+fileAmount);
		session.setAttribute("msgSuccussful", "索引已创建，共添加"+fileAmount+"个文档。");
		response.sendRedirect("/luceneweb/index.jsp");
	}

}
