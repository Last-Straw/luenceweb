<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="lucenePackage.Results"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@include file="header.jsp"%><!-- 引入静态页面：直接把文件内容读进来，不做任何处理 -->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

  <head>
    <base href="<%=basePath%>">
    
    <title>Lucene2.4 Information Retrieval System</title>
	<meta http-equiv="pragma" content="no-cache"><!-- 用于设定禁止浏览器从本地机的缓存中调阅页面内容 ，设定后一旦离开网页就无法从cache中再调出-->
	<meta http-equiv="cache-control" content="no-cache"><!-- 清除缓存 -->
	<meta http-equiv="expires" content="0"><!-- 用于设定网页的到期时间。一旦网页过期，必须到服务器上重新传输 -->    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"><!-- 关键字，给搜索引擎用的 -->
	<meta http-equiv="description" content="This is my page"><!-- 页面描述 -->
	<link rel="stylesheet" type="text/css" href="style.css" /><!-- 告诉浏览器，这是一个css层叠样式表，表示这个css文件链接到style.css这个地方 -->
  </head>
  
  <body>
  <%
  	String DocDirectory="";//文档路径
    	String IndexDirectory="";  //索引路径
    	
    	//用来得到msgDocDirecotry名称的session值，即得到docDirectory对象，注意需要进行类型转换！
    	String msgDocDirecotry = (String)session.getAttribute("msgDocDirecotry");//获取request域中的名为msg的属性
  	if(msgDocDirecotry != null) {
  		DocDirectory = msgDocDirecotry;
  	}
  	String msgIndexDirecotry = (String)session.getAttribute("msgIndexDirecotry");//获取request域中的名为msg的属性
  	if(msgIndexDirecotry != null) {
  		IndexDirectory = msgIndexDirecotry;
  	}
  	String IndexInfo="";
  	String msgIndexInfo;
  	msgIndexInfo = (String)session.getAttribute("msgSuccussful");
  	if(msgIndexInfo !=null){
  		IndexInfo=msgIndexInfo;
  	}
  	int fileAllAmount=0;
  	String msgfileAllAmount= (String)session.getAttribute("fileAmount");
  	if(msgfileAllAmount!=null){
  		fileAllAmount=Integer.parseInt(msgfileAllAmount);
  	}
  %>
  <div class="body"> 
    <form action="/luceneweb/lucenePackage/indexCreateServlet" method="post">
   	<div class="style1">
    	<table align="center" >
    
    		<tr><!-- 行 -->
    			<td><!-- 列 -->
    				文档路径：<input type="text" name="docDirectory" value="<%=DocDirectory%>">
    			</td>
    			<td rowspan="2"><!-- row行：合并两行 -->    			
					&nbsp;&nbsp;&nbsp;&nbsp;<input class="button" type="submit" value="建立索引" >
    			</td>
    		</tr>
    		<tr>
    			<td>
    				索引路径：<input type="text" name="indexDirectory" value="<%=IndexDirectory%>">
    			</td>
    		</tr>
    		  <tr>
    			<td colspan="2" align="center">
    				<font color="red"><h3><%=IndexInfo%></h3></font>
    			</td>
    		</tr>
    		
    	</table>
   	</div>
    </form>
    <!-- <hr>   创建一条水平线 --> 
    
   
   <%
    	int currentPage=0;
    	int currentNum=0;
    	int totalPage=0;
    	int totalHitsNum=-1;
    	String pageNumShow="";
    	
    	
    	String queryString="";
    	String maxresults="5";
    	
    	String resultMsg="";
    	lucenePackage.Results result=null;
        lucenePackage.Results revResult=(lucenePackage.Results)session.getAttribute("results");//从servlet传递数据给jsp
        if(revResult!=null){
       		result=revResult;
       		resultMsg="共找到"+result.totalHits+"篇相关文档。";  //查询结果
       		totalHitsNum=result.totalHits;
       	}
       	
       	
       	String queryStringMsg=(String)session.getAttribute("queryString");
       	String maxresultsMsg=(String)session.getAttribute("maxresults");
       	if(queryStringMsg!=null&&maxresultsMsg!=null){
       		queryString=queryStringMsg;
       		maxresults=maxresultsMsg;
       	}
       	
       	if(totalHitsNum==0){
    		currentPage=0;
    		totalPage=0;
    		currentNum=0;
    		}
    		if(totalHitsNum>0){
    			currentPage=1;
    			totalPage=(totalHitsNum-1)/(Integer.parseInt(maxresults))+1;
    			if(totalPage>1)currentNum=Integer.parseInt(maxresults);
    			else currentNum=totalHitsNum;
    		}
    		if(totalHitsNum>-1){
    			pageNumShow="当前是第"+currentPage+"页，共"+totalPage+"页。";//查询结果:当前是第1页，共3页。
    		}
    		
    		
    		
    		String currentPageMsg=(String)session.getAttribute("currentPage");
        	String currentNumMsg=(String)session.getAttribute("currentNum");
        	String totalPageMsg=(String)session.getAttribute("totalPage");
        	String totalHitsNumMsg=(String)session.getAttribute("totalHitsNum");
        	if(currentPageMsg!=null&currentNumMsg!=null&&totalPageMsg!=null&&totalHitsNumMsg!=null){
        		currentPage=Integer.parseInt(currentPageMsg);
        		currentNum=Integer.parseInt(currentNumMsg);
        		totalPage=Integer.parseInt(totalPageMsg);
        		totalHitsNum=Integer.parseInt(totalHitsNumMsg);
        		//pageNumShow="当前是第"+currentPage+"页，共"+totalPage+"页。";
        	}
           	
    
    %>
    
    <div class="style2">
    <form action="/luceneweb/lucenePackage/searchServlet" method="post">
    	<input type="hidden" name="indexDirectory" value="<%=IndexDirectory%>">
    	<input type="hidden" name="fileAllAmount" value="<%=fileAllAmount%>">
    	<input type="hidden" name="currentPage" value="<%=currentPage%>">
    	<input type="hidden" name="currentNum" value="<%=currentNum%>">
    	<input type="hidden" name="totalPage" value="<%=totalPage%>">
    	<input type="hidden" name="totalHitsNum" value="<%=totalHitsNum%>">
    	
    	<table align="center">
    		<tr>
    			<td>
    				查询关键词：<input type="text" size="44" name="queryString" value="<%=queryString%>">
    			</td>
    			<td>
					&nbsp;&nbsp;每页显示个数：<input name="maxresults" size="4" value="<%=maxresults%>"/>
    			</td>
    			<td>
    				&nbsp;&nbsp;&nbsp;&nbsp;<input class="button" type="submit" value="查询">
    			</td>
    		</tr>
    		<tr>
    			<td>
    				查询结果：<%= resultMsg%><%= pageNumShow%>
    			</td>
    		</tr>
    	</table>
    </form>
    
    <!-- border-collapse:collapse为表格设置合并边框模型       cellspacing="0"边框与边框之间的间隙大小 -->
    <table id="resultTable" align="center" width="450px" style="table-layout:fixed;border-collapse:collapse" border="1px" cellspacing="0">
    	<tr>
    		<th width="50px" >
    			
    		</th>
    		<th width="300px">
    			文档路径
    		</th>
    	</tr>
    	<%
    		if(result!=null){
    			int x=currentNum;
    			int start=(currentPage-1)*Integer.parseInt(maxresults)+1;   //定义当前页排名的起始编号
    			int end=start+x-1;                 //定义当前页排名的末尾编号
    			for(int i=start;i<=end;i++){
    				out.println("<tr>");
    				out.println("<td align=\"center\">");
    				out.println(i);
    				out.println("</td>");//排名那一列下方的内容：1、2、3、4、5....
    				out.println("<td align=\"center\">");
    				out.println(result.dir.get(i-1));
    				out.println("</td>");//文档路径那列下方的内容
    				out.println("</tr>");
    			}
    		}
    	 %>
    	 
    </table>
    
    <table align="center">
    	<tr>
    		<td>&nbsp;&nbsp;&nbsp;&nbsp;
    		<form action="/luceneweb/lucenePackage/firstPageServlet" method="post">
    			<input type="hidden" name="currentPage" value="<%=currentPage%>">
    			<input type="hidden" name="currentNum" value="<%=currentNum%>">
    			<input type="hidden" name="totalPage" value="<%=totalPage%>">
    			<input type="hidden" name="totalHitsNum" value="<%=totalHitsNum%>">
    			<input type="hidden" name="maxresults" value="<%=maxresults%>">
    			<input class="button" type="submit" value="首页">
    		</form>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		</td>
    		
    		<td>&nbsp;&nbsp;&nbsp;&nbsp;
    		<form action="/luceneweb/lucenePackage/prevPageServlet" method="post">
    		<input type="hidden" name="currentPage" value="<%=currentPage%>">
    			<input type="hidden" name="currentNum" value="<%=currentNum%>">
    			<input type="hidden" name="totalPage" value="<%=totalPage%>">
    			<input type="hidden" name="totalHitsNum" value="<%=totalHitsNum%>">
    			<input type="hidden" name="maxresults" value="<%=maxresults%>">
    			<input class="button" type="submit" value="上一页">
    		</form>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		</td>
    		
    		<td>&nbsp;&nbsp;&nbsp;&nbsp;
    		<form action="/luceneweb/lucenePackage/nextPageServlet" method="post">
    		<input type="hidden" name="currentPage" value="<%=currentPage%>">
    			<input type="hidden" name="currentNum" value="<%=currentNum%>">
    			<input type="hidden" name="totalPage" value="<%=totalPage%>">
    			<input type="hidden" name="totalHitsNum" value="<%=totalHitsNum%>">
    			<input type="hidden" name="maxresults" value="<%=maxresults%>">
    			<input class="button" type="submit" value="下一页">
    		</form>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		</td>
    		
    		<td>&nbsp;&nbsp;&nbsp;&nbsp;
    		<form action="/luceneweb/lucenePackage/lastPageServlet" method="post">
    		<input type="hidden" name="currentPage" value="<%=currentPage%>">
    			<input type="hidden" name="currentNum" value="<%=currentNum%>">
    			<input type="hidden" name="totalPage" value="<%=totalPage%>">
    			<input type="hidden" name="totalHitsNum" value="<%=totalHitsNum%>">
    			<input type="hidden" name="maxresults" value="<%=maxresults%>">
    			<input class="button" type="submit" value="尾页">
    		</form>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		</td>	
    	</tr>
    </table>
   </div>
  </div>  
  </body>
</html>
