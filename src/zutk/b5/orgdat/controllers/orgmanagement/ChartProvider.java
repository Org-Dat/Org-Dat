/**
 * This Servlet make chart view  for table
 * 
 * @author : Obeth Samuel 
 * 
 * @version : 1.0
 */
package zutk.b5.orgdat.controllers.orgmanagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import zutk.b5.orgdat.model.orgmanagement.ChartPrivers;
import zutk.b5.orgdat.controllers.filters.*;

public class ChartProvider extends HttpServlet {
	DatabaseConnection dc;
	PrintWriter out;
		@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) {
		try {
		    out = response.getWriter();
			if (request.getMethod().toLowerCase().equals("post")) {
				doGet(request, response);
			} else {
			    
				out.write("{'status':405,'message':'this get only url'}");
			}
		} catch (Exception e) {
             out.write("{'status':405,'message':'this post only url'}");
		}

	}
	/**
	 * This method check and provider chart
	 * 
	 * @params : HttpServletRequest request , HttpServletResponse response
	 * 
	 * @return : if user correct column it return chart object .else return error object
	 */
		@Override 
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
		    out = response.getWriter();
            ChartPrivers cp = new ChartPrivers();
			String chart = request.getParameter("charts");
			String org_name = request.getParameter("org_name");
			String db_name = request.getParameter("db_name");
			String table_name = request.getParameter("table_name");
			if (cp.isCorrect(org_name) == false || cp.isCorrect(db_name) == false
					|| cp.isCorrect(table_name) == false) {
				throw new Exception();
			}
			dc = new DatabaseConnection(db_name, org_name, "");
			String[] column = request.getParameterValues("column_names");
			if(column.length < 2){
			    throw new Exception();
			}
			String detailArray = cp.getDetail(table_name, column);
			String[] chartList = request.getParameterValues("chartNames");
			for(String chart_name : chartList){
			    
			}
			switch (chart) {
			case "pieChart":
				String is3D = request.getParameter("is3D");
				out.write(cp.pieChartWriter(table_name, is3D, detailArray));
				break;
			case "columnChart":
				String subTitle = request.getParameter("subtitle");
				String bars = request.getParameter("bars");
				out.write(cp.columnChart(detailArray, table_name, subTitle, bars));
				break;
			case "bubbleChart":
				String hAxis = request.getParameter("hAxis");
				String vAxis = request.getParameter("vAxis");
				out.write(cp.bubbleChart(detailArray, table_name, hAxis, vAxis));
				break;
			case "lineChart":
				out.write(cp.lineChart(detailArray, table_name));
				break;
			case "scatterChart":
				String hxis = request.getParameter("hAxis");
				String vxis = request.getParameter("vAxis");
				out.write(cp.scatterChart(detailArray, table_name, hxis, vxis));
				break;
			default:
				throw new Exception();
			}

		} catch (Exception e) {
            out.write("invaild input");
		}
	}
}