<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawSeriesChart);

    function drawSeriesChart() {

      var data = google.visualization.arrayToDataTable(<%=request.getParameter("detailArray")%>);

      var options = {
        title: <%=request.getParameter("title")%>,
        hAxis: {title: <%=request.getParameter("hAxis")%>},
        vAxis: {title: <%=request.getParameter("vAxis")%>},
        bubble: {textStyle: { fontSize: 12,
        fontName: 'Times-Roman',
        color: 'green',
        bold: true,
        italic: true
         }}
      };

      var chart = new google.visualization.BubbleChart(document.getElementById('series_chart_div'));
      chart.draw(data, options);
    }
    </script>
  </head>
  <body>
    <div id="series_chart_div" style="width: 720px; height: 400px;"></div>
  </body>
</html>
