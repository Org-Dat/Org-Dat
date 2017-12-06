<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['bar']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable(<%=request.getParameter("detailArray")%>);

        var options = {
          chart: {
            title: <%=request.getParameter("title")%>,
            subtitle: <%=request.getParameter("sub_title")%>,
         }
           bars: <%=request.getParameter("bars")%>
        };

        var chart = new google.charts.Bar(document.getElementById('columnchart_material'));

        chart.draw(data, google.charts.Bar.convertOptions(options));
      }
    </script>
  </head>
  <body>
    <div id="columnchart_material" style="width: 720px; height: 400px;"></div>
  </body>
</html>
