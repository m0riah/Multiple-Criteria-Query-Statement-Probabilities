<!DOCTYPE HTML>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:url scope="page" var="canvasjs"
            value="/resources/canvasjs.min.js"/>

<spring:url scope="page" var="jqueryUrl"
            value="/resources/jquery-1.9.1.min.js"/>

<html>
<head>
    <script type="text/javascript">
        window.onload = function () {

            var queryTransitionProbY = 1;
            var queryProbY = 1;
            var orcnStartTime;
            var maxDisplayedElements = 60;
            var totalCount = 0;
            var orcnQueryTransitionProbSum = 0;
            var orcnQueryProbSum = 0;
            var orcnQueryTransitionProvAvg = [];
            var orcnQueryProbAvg = [];
            var orcnQueryTransitionProbLastFiveAvg = [];
            var orcnQueryProbLastFiveAvg = [];
            var orcnQueryTransitionProb = [];
            var orcnQueryProb = [];


            <%--ORCN Query Probabilities Time Series Graph configuration--%>
            var orcnChart = new CanvasJS.Chart("orcnChartContainer", {
                zoomEnabled: true,
                title: {
                    text: "ORCN Query Probabilities Time Series Graph"
                },
                toolTip: {
                    shared: true
                },
                legend: {
                    verticalAlign: "top",
                    horizontalAlign: "center",
                    fontSize: 14,
                    fontWeight: "bold",
                    fontFamily: "calibri",
                    fontColor: "dimGrey"
                },
                axisX: {
                    title: "Time"
                },
                axisY: {
                    title: "Probability"
                },
                data: [
//                    {
//                        // query transition probability
//                        type: "line",
//                        xValueType: "dateTime",
//                        showInLegend: true,
//                        name: "Query Transition Prob.",
//                        dataPoints: orcnQueryTransitionProb
//                    },
                    {
                        // query transition probability last five average.
                        type: "line",
                        xValueType: "dateTime",
                        showInLegend: true,
                        name: "Query Transition Prob Last Five Avg.",
                        dataPoints: orcnQueryTransitionProbLastFiveAvg
                    },
                    {
                        // query transition probability average.
                        type: "line",
                        xValueType: "dateTime",
                        showInLegend: true,
                        name: "Query Transition Prob Avg.",
                        dataPoints: orcnQueryTransitionProvAvg
                    },
//                    {
//                        // query probability.
//                        type: "line",
//                        xValueType: "dateTime",
//                        showInLegend: true,
//                        name: "Query Prob.",
//                        dataPoints: orcnQueryProb
//                    },
                    {
                        // query probability last five average.
                        type: "line",
                        xValueType: "dateTime",
                        showInLegend: true,
                        name: "Query Prob Last Five Avg.",
                        dataPoints: orcnQueryProbLastFiveAvg
                    },
                    {
                        // query probability average.
                        type: "line",
                        xValueType: "dateTime",
                        showInLegend: true,
                        name: "Query Prob Avg.",
                        dataPoints: orcnQueryProbAvg
                    }
                ]
            });

            var updateInterval = 1000;
            orcnStartTime = new Date();


            var updateOrcnChart = function (count) {
                count = count || 1;
                var orcnTempTime;
                var currentLength = orcnQueryProb.length;
                if ( currentLength > maxDisplayedElements) {
                    var totalSlicingNumber = currentLength - maxDisplayedElements;
                    orcnQueryProb.splice(0, totalSlicingNumber);
                    orcnQueryProbAvg.splice(0, totalSlicingNumber);
                    orcnQueryProbLastFiveAvg.splice(0, totalSlicingNumber);
                    orcnQueryTransitionProb.splice(0, totalSlicingNumber);
                    orcnQueryTransitionProvAvg.splice(0, totalSlicingNumber);
                    orcnQueryTransitionProbLastFiveAvg.splice(0, totalSlicingNumber);
                }

                $.getJSON('../api/orcn/pbqlogs', {
                    start: orcnStartTime.getTime(),
                    end: new Date().getTime(),
                    userName: 'user1',
                    ajax: 'true'
                }, function (data) {
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        orcnTempTime = new Date(data[i].loggingTime);
                        orcnQueryTransitionProbSum += data[i].queryTransitionProbability;
                        orcnQueryProbSum += data[i].queryProbability;
                        totalCount += 1;
                        queryTransitionProbY = data[i].queryTransitionProbability;
                        queryProbY = data[i].queryProbability;

                        // push the remaining query transition probabilities into the chart from the json array.
                        orcnQueryTransitionProb.push({
                            x: orcnTempTime,
                            y: queryTransitionProbY
                        });
                        var queryTransitionProbLastFiveSumTemp = 0;
                        for (var j = orcnQueryTransitionProb.length - 5; j < orcnQueryTransitionProb.length; j++) {
                            queryTransitionProbLastFiveSumTemp += orcnQueryTransitionProb[j].y;
                        }
                        // push the last five average of query transition probabilities.
                        orcnQueryTransitionProbLastFiveAvg.push({
                            x: orcnTempTime,
                            y: queryTransitionProbLastFiveSumTemp / 5
                        })
                        // push the average of query transition probabilities.
                        orcnQueryTransitionProvAvg.push({
                            x: orcnTempTime,
                            y: orcnQueryTransitionProbSum / totalCount
                        })

                        // push the remaining query probabilities into the chart from the json array.
                        orcnQueryProb.push({
                            x: orcnTempTime,
                            y: queryProbY
                        });
                        var queryProbLastFiveSumTemp = 0;
                        for (var j = orcnQueryProb.length - 5; j < orcnQueryProb.length; j++) {
                            queryProbLastFiveSumTemp += orcnQueryProb[j].y;
                        }
                        // push the last five average of query probabilities.
                        orcnQueryProbLastFiveAvg.push({
                            x: orcnTempTime,
                            y: queryProbLastFiveSumTemp / 5
                        })
                        // push the average of query probabilities.
                        orcnQueryProbAvg.push({
                            x: orcnTempTime,
                            y: orcnQueryProbSum / totalCount
                        })
                    }
                    if (orcnTempTime != null) {
                        orcnStartTime = orcnTempTime;
                    }
                });

                // updating legend text with  updated with y Value
//                orcnChart.options.data[0].legendText = "Query Transition Prob.";
                orcnChart.options.data[0].legendText = "Query Transition Prob Last Five Avg.";
                orcnChart.options.data[1].legendText = "Query Transition Prob Avg.";
//                orcnChart.options.data[3].legendText = "Query Prob.";
                orcnChart.options.data[2].legendText = "Query Prob Last Five Avg.";
                orcnChart.options.data[3].legendText = "Query Prob Avg.";
                orcnChart.render();

            };

            // generates first set of dataPoints
            updateOrcnChart();

            // update chart after specified interval
            setInterval(function () {
                updateOrcnChart()
            }, updateInterval);
        }
    </script>
    <script src="${pageScope.canvasjs}"></script>
    <script src="${pageScope.jqueryUrl}"></script>
</head>
<body>
<h2>${message}</h2>
<a href="../">QCTN Query Probabilities Time Series Graph</a>

<div id="orcnChartContainer" style="height: 300px; width: 100%;"/>

</body>
</html>