<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>

	<!-- Access the bootstrap Css like this,
    		Spring boot will handle the resource mapping automcatically -->
    	<link rel="stylesheet" type="text/css" href="webjars/bootstrap/4.6.0/css/bootstrap.min.css" />
        <link rel="stylesheet" href="/css/main.css">
        <link rel="stylesheet" href="/css/Chart.min.css">

</head>
<body onload='renderChart()'>

	<div class="container">
		<div class="row justify-content-center">
            <div class="col-auto mt-5">
                <select id="offenceCodeGroup">
                    <option value="select" selected>OFFENCE CODE GROUP</option>
                    <c:forEach var="offenceCode" items="${offenceCodeGroup}">
                            <option value='<c:out value="${offenceCode.key}" />'><c:out value="${offenceCode.value}" /></option>
                    </c:forEach>
                </select>
            </div>
		</div>
	</div>

	<div class="container">
        <div class="row justify-content-center">
            <div class="col-auto mt-3" style="width:75%;">
                 <canvas id="myChart" width="800" height="600"></canvas>
            </div>
        </div>
	</div>
    <div id="loader" class="lds-dual-ring display-none overlay"></div>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/4.6.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/js/Chart.min.js"></script>
    <script>
        function renderChart() {

             var selected = $('#offenceCodeGroup').val();
             if(selected != 'select') {
                    $.ajax({
                        url: '/data/'+selected,
                        data: {
                            id: {selected},
                        },
                        beforeSend: function () {
                            $('#loader').removeClass('display-none');
                        },
                        success: function(result) {
                            parseResultSet(result);
                        },
                        complete: function () {
                            $('#loader').addClass('display-none');
                        },
                    });
             } else {
                    $.ajax({
                        url: '/data/',
                        beforeSend: function () {
                            $('#loader').removeClass('display-none');
                        },
                        success: function(result) {
                            parseResultSet(result);
                        },
                        complete: function () {
                             $('#loader').addClass('display-none');
                        },
                    });
             }
        }

        function parseResultSet(result) {
               var labelArr = [];
               var dataArr = [];
               for(var e in result) {
                    var dataCopy = result[e];
                    for(key in dataCopy) {
                            if(key == 'date') {
                                labelArr.push(new String(dataCopy[key]));
                            }else if(key == 'count') {
                                dataArr.push(new Number(dataCopy[key]));
                            }
                    }
               }
               var config = {
                    type: 'line',
                    data: {
                     labels: labelArr,
                     datasets: [{
                         label: '# of Crimes',
                         backgroundColor: 'rgba(54, 162, 235, 0.2)',
                         borderColor: 'rgba(54, 162, 235, 0.2)',
                         data: dataArr,
                         borderColor: 'rgba(54, 162, 235, 1)',
                         fill: false,
                     }]
                    },
                    options: {
                        legend: { display: false },
                          title: {
                            display: true,
                            text: '# of Crimes'
                        },
                        events: ['click'],
                        scales: {
                            x: {
                                display: true,
                            },
                            y: {
                                display: true,
                            }
                        }
                    }
               };
               var ctx = document.getElementById('myChart');
               var myChart = new Chart(ctx, config);
        }

        $(document).ready(function() {
              $('#offenceCodeGroup').on('change', function() {
                    renderChart();
              })
        })
    </script>
</body>

</html>