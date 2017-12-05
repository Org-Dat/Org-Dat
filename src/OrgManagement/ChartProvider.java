package OrgManagement;

import java.util.ArrayList;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import Filters.DatabaseConnection;
import java.sql.*;

public class ChartProvider extends HttpServlet {
	DatabaseConnection dc;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {

			String chart = request.getParameter("charts");
			String org_name = request.getParameter("org_name");
			String db_name = request.getParameter("db_name");
			String table_name = request.getParameter("table_name");
			if (isCorrect(org_name) == false || isCorrect(db_name) == false
					|| isCorrect(table_name) == false) {
				throw new Exception();
			}
			dc = new DatabaseConnection(db_name, org_name, "");
			String[] column = request.getParameterValues("column_names");
			String detailArray = getDetail(table_name, column);
			switch (chart) {
			case "pieChart":
				String value = request.getParameter("value");
				response.sendRedirect("/pieChart?value=" + value + "&title="
						+ table_name + "&detailArray=" + detailArray);
				break;
			case "columnChart":
				String subTitle = request.getParameter("subtitle");
				String bars = request.getParameter("bars");
				response.sendRedirect("/columnChart?bars=" + bars + "&title="
						+ table_name + "sub_title=" + subTitle
						+ "&detailArray=" + detailArray);
				break;
			case "bubbleChart":
				String hAxis = request.getParameter("hAxis");
				String vAxis = request.getParameter("vAxis");
				response.sendRedirect("/bubbleChart?hAxis=" + hAxis + "&vAxis="
						+ vAxis + "&detailArray" + detailArray + "&title="
						+ table_name);
				break;
			case "lineChart":
				response.sendRedirect("/lineChart?title=" + table_name
						+ "&detailArray=" + detailArray);
				break;
			case "scatterChart":
				String hxis = request.getParameter("hAxis");
				String vxis = request.getParameter("vAxis");
				response.sendRedirect("/scatterChart?hAxis=" + hxis + "&vAxis="
						+ vxis + "&detailArray" + detailArray + "&title="
						+ table_name);
				break;
			default:
				throw new Exception();
			}

		} catch (Exception e) {

		}
	}

	private boolean isCorrect(String name) {
		return name.matches("^[a-z][a-z0-9]{5,40}");
	}

	private String getDetail(String table_name, String[] column_names) {
		try {
			String sql = "select ";
			for (String column : column_names) {
				sql = sql + "," + column;
			}
			sql = sql + " from " + table_name;
			dc.stmt = dc.conn.prepareStatement(sql);
			ResultSet rs = dc.stmt.executeQuery();
			ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
			ArrayList<String> eachRecord = new ArrayList<String>();
			for (String column : column_names) {
				eachRecord.add(column);
			}
			records.add(eachRecord);
			while (rs.next()) {
				eachRecord.clear();
				for (int i = 0; i < column_names.length; i++) {
					Object element = rs.getObject(i);
					eachRecord.add((String) element);
				}
			}
			return records.toString();
		} catch (Exception e) {
			return "";
		}
	}
}