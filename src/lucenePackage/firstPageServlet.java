package lucenePackage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class firstPageServlet extends HttpServlet {

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
		String NcurrentPage=request.getParameter("currentPage");
		String NcurrentNum=request.getParameter("currentNum");
		String NtotalPage=request.getParameter("totalPage");
		String NtotalHitsNum=request.getParameter("totalHitsNum");
		String Nmaxresults=request.getParameter("maxresults");
		
		int currentPage=Integer.parseInt(NcurrentPage);
		int currentNum=Integer.parseInt(NcurrentNum);
		int totalPage=Integer.parseInt(NtotalPage);
		int totalHitsNum=Integer.parseInt(NtotalHitsNum);
		int maxresults=Integer.parseInt(Nmaxresults);
		if(totalHitsNum==0){
			currentPage=0;
			totalPage=0;
			currentNum=0;
		}
		if(totalHitsNum>0){
			currentPage=1;
			totalPage=(totalHitsNum-1)/(maxresults)+1;
			if(totalPage>1)currentNum=maxresults;
			else currentNum=totalHitsNum;
		}

		HttpSession session = request.getSession();//ªÒ»°session
		session.setAttribute("currentPage", ""+currentPage);
		session.setAttribute("currentNum",""+currentNum);
		session.setAttribute("totalPage", ""+totalPage);
		session.setAttribute("totalHitsNum",""+totalHitsNum);
		response.sendRedirect("/luceneweb/index.jsp");
	}

}
