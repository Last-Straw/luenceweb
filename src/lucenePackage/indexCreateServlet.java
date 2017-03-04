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
			fileAmount=IndexFiles.startCreate(indexDirectory, docDirectory);//IndexFiles.startCreate��ʼ��������
		} catch (Exception e) {//��try����г����쳣��ʱ����ִ��catch�е����
			// TODO Auto-generated catch block   
			e.printStackTrace();//�������д�ӡ�쳣��Ϣ�ڳ����г����λ�ü�ԭ��
		}
		HttpSession session = request.getSession();//��ȡsession  ���ص���httpSession ���͵ģ��Ӻ�̨servlet��jsp��ʱ������ݷŵ�session�������jspҳ�档
		//session.setAttribute("sessionName",Object);
		//��������sessionֵ�ģ�sessionName�����ƣ�object����Ҫ����Ķ���
		session.setAttribute("msgDocDirecotry", docDirectory);
		session.setAttribute("msgIndexDirecotry", indexDirectory);
		session.setAttribute("fileAmount", ""+fileAmount);
		session.setAttribute("msgSuccussful", "�����Ѵ����������"+fileAmount+"���ĵ���");
		response.sendRedirect("/luceneweb/index.jsp");
	}

}
