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
		String maxresults=request.getParameter("maxresults");//��ǰҳ����ʾ��������ѯ�����
		String totalAlldoc=request.getParameter("fileAllAmount");
		Results result=null;
		try {
			result=SearchFiles.doSearch(indexDirectory,queryString,maxresults,Integer.parseInt(totalAlldoc));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String NcurrentPage=request.getParameter("currentPage");//��ǰҳ
		String NcurrentNum=request.getParameter("currentNum");//��ǰҳ��������ʾ���Ĳ�ѯ�ĸ���
		String NtotalPage=request.getParameter("totalPage");//��ҳ��
		String NtotalHitsNum=request.getParameter("totalHitsNum");//һ����ѯ���ĸ���
		
		//Integer.parseint()���ǰ����ζ���Integerת���ɻ�����������int����������
		int currentPage=Integer.parseInt(NcurrentPage);
		int currentNum=Integer.parseInt(NcurrentNum);
		int totalPage=Integer.parseInt(NtotalPage);
		int totalHitsNum=result.totalHits;
		int maxresult=Integer.parseInt(maxresults);
		//ʵ�ַ�ҳ
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
		
		//���ص���httpSession ���͵ģ��Ӻ�̨servlet��jsp��ʱ������ݷŵ�session�������jspҳ�档
		//session.setAttribute("sessionName",Object);
		//��������sessionֵ�ģ�sessionName�����ƣ�object����Ҫ����Ķ���
		HttpSession session = request.getSession();//��ȡsession  
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
