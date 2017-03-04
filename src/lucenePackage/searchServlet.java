package lucenePackage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class searchServlet extends HttpServlet {

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
		String indexDirectory=request.getParameter("indexDirectory");
		String queryString=request.getParameter("queryString");
		String maxresults=request.getParameter("maxresults");//当前页能显示出的最大查询结果，
		String totalAlldoc=request.getParameter("fileAllAmount");
		Results result=null;
		try {
			result=SearchFiles.doSearch(indexDirectory,queryString,maxresults,Integer.parseInt(totalAlldoc));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String NcurrentPage=request.getParameter("currentPage");//当前页
		String NcurrentNum=request.getParameter("currentNum");//当前页面所能显示到的查询的个数
		String NtotalPage=request.getParameter("totalPage");//总页数
		String NtotalHitsNum=request.getParameter("totalHitsNum");//一共查询到的个数
		
		//Integer.parseint()就是把整形对象Integer转换成基本数据类型int（整数）。
		int currentPage=Integer.parseInt(NcurrentPage);
		int currentNum=Integer.parseInt(NcurrentNum);
		int totalPage=Integer.parseInt(NtotalPage);
		int totalHitsNum=result.totalHits;
		int maxresult=Integer.parseInt(maxresults);
		//实现分页
		if(totalHitsNum==0){
			currentPage=0;
			totalPage=0;
			currentNum=0;
		}
		if(totalHitsNum>0){
			currentPage=1;
			totalPage=(totalHitsNum-1)/(maxresult)+1;
			if(totalPage>1)currentNum=maxresult;
			else currentNum=totalHitsNum;
		}
		
		//返回的是httpSession 类型的，从后台servlet到jsp的时候把数据放到session里给传到jsp页面。
		//session.setAttribute("sessionName",Object);
		//用来设置session值的，sessionName是名称，object是你要保存的对象。
		HttpSession session = request.getSession();//获取session  
		session.setAttribute("currentPage", ""+currentPage);
		session.setAttribute("currentNum",""+currentNum);
		session.setAttribute("totalPage", ""+totalPage);
		session.setAttribute("totalHitsNum",""+totalHitsNum);
		
		session.setAttribute("queryString", queryString);
		session.setAttribute("maxresults",maxresults);
		session.setAttribute("results", result);
		response.sendRedirect("/luceneweb/index.jsp");
	}

}
