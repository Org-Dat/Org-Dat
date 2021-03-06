package zutk.b5.orgdat.model.orgmanagement;

import java.sql.*;
import zutk.b5.orgdat.controllers.filters.*;
import java.util.*;

public class ChartPrivers {
	DatabaseConnection dc;

	public boolean isCorrect(String name) {
		return name.matches("^[a-z][a-z0-9]{3,30}");
	}

	public void initialise(String user, String password, String db_name) {
		this.dc = new DatabaseConnection(user, password, db_name);
	}

	public String getDetail(String table_name, String[] column_names) {
		try {
			String sql = "select ";
			int count = 0;
			for (String column : column_names) {
			    if(count == 0){
			        sql = sql +" " + column;
			    }else{
			        sql = sql + "," + column;
			    }
			    count++;
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
			return "invaild input";
		}
	}

	public String pieChartWriter(String title, String is3D, String detailArray) {
		try {
			StringBuilder pieChart = new StringBuilder("");
			pieChart.append("<div id='curve_chart' style='width: 900px; height: 500px'></div><script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script><script type='text/javascript'>");
			pieChart.append("google.charts.load('current', {'packages':['corechart']});google.charts.setOnLoadCallback(drawChart);function drawChart() {");
			pieChart.append(" var data = google.visualization.arrayToDataTable("
					+ detailArray + "]);var options = {");
			pieChart.append("title :" + title + ",is3D :" + is3D);
			pieChart.append("};var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));");
			pieChart.append("}chart.draw(data, options);</script>");
			return pieChart.toString();
		} catch (Exception e) {
			return "invaild input";
		}
	}

	public String bubbleChart(String detailArray, String title, String hAxis,
			String vAxis) {
		try {
			StringBuilder bubbleChart = new StringBuilder("");
			bubbleChart
					.append("<div id='series_chart_div' style='width: 720px; height: 400px;'></div><script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script><script>");
			bubbleChart
					.append("var data = google.visualization.arrayToDataTable("
							+ detailArray + ");var options = {");
			bubbleChart
					.append("title :"
							+ title
							+ ",hAxis :"
							+ hAxis
							+ ",vAxis : "
							+ vAxis
							+ ",bubble: {textStyle: { fontSize: 12,fontName: 'Times-Roman',color: 'green',bold: true,italic: true }} };");
			bubbleChart
					.append("var chart = new google.visualization.BubbleChart(document.getElementById('series_chart_div')); chart.draw(data, options);}</script>");
			return bubbleChart.toString();
		} catch (Exception e) {
			return "invaild input";
		}
	}

	public String lineChart(String detailArray, String title) {
		try {
			StringBuilder lineChart = new StringBuilder("");
			lineChart
					.append("<div id='curve_chart' style='width: 720px; height: 400px'></div>");
			lineChart
					.append("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script><script type='text/javascript'>google.charts.load('current', {'packages':['corechart']});google.charts.setOnLoadCallback(drawChart); function drawChart() {");
			lineChart
					.append("var data = google.visualization.arrayToDataTable("
							+ detailArray
							+ ");var options = { curveType: 'function',legend: { position: 'bottom' } };");
			lineChart
					.append("var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));chart.draw(data, options); } </script>");
			return lineChart.toString();
		} catch (Exception e) { 
			return "invaild input";
		}
	}

	public String scatterChart(String detailArray, String title, String hAxis,
			String vAxis) {
		try {
			StringBuilder scatterChart = new StringBuilder("");
			scatterChart
					.append("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script><script type='text/javascript'>google.charts.load('current', {'packages':['corechart']}); google.charts.setOnLoadCallback(drawChart); function drawChart() {");
			scatterChart
					.append("var data = google.visualization.arrayToDataTable("
							+ detailArray + ");var options = {");
			scatterChart.append("title :" + title + ",hAxis :" + hAxis
					+ ",vAxis : " + vAxis + ",legend: 'none';");
			scatterChart
					.append(" var chart = new google.visualization.ScatterChart(document.getElementById('chart_div'));chart.draw(data, options);}</script>");
			return scatterChart.toString();
		} catch (Exception e) {
			return "invaild input";
		}
	}

	public String columnChart(String detailArray, String title,
			String subtitle, String bars) {
		try {
			StringBuilder columnChart = new StringBuilder("");
			columnChart.append("<div id='columnchart_material' style='width: 720px; height: 400px;'></div>");
			columnChart
					.append("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script><script type='text/javascript'> google.charts.load('current', {'packages':['bar']}); google.charts.setOnLoadCallback(drawChart);function drawChart() {");
			columnChart
					.append("var data = google.visualization.arrayToDataTable("
	  						+ detailArray + ");var options = { chart: {");
			columnChart.append("title : " + title + ",subtitle :" + subtitle
					+ "},bars : " + bars + " };");
			columnChart
					.append("var chart = new google.charts.Bar(document.getElementById('columnchart_material'));chart.draw(data, google.charts.Bar.convertOptions(options));}</script>");
			return columnChart.toString();
		} catch (Exception e) {
			return "invaild input";
		}
	}

}