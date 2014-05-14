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
            var qctnStartTime;
            var maxDisplayedElements = 60;
            var totalCount = 0;
            var qctnQueryTransitionProbSum = 0;
            var qctnQueryProbSum = 0;
            var qctnQueryTransitionProvAvg = [];
            var qctnQueryProbAvg = [];
            var qctnQueryTransitionProbLastFiveAvg = [];
            var qctnQueryProbLastFiveAvg = [];
            var qctnQueryTransitionProb = [];
            var qctnQueryProb = [];


            <%--QCTN Query Probabilities Time Series Graph configuration--%>
            var qctnChart = new CanvasJS.Chart("qctnChartContainer", {
                zoomEnabled: true,
                title: {
                    text: "QCTN Query Probabilities Time Series Graph"
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
//                        dataPoints: qctnQueryTransitionProb
//                    },
                    {
                        // query transition probability last five average.
                        type: "line",
                        xValueType: "dateTime",
                        showInLegend: true,
                        name: "Query Transition Prob Last Five Avg.",
                        dataPoints: qctnQueryTransitionProbLastFiveAvg
                    },
                    {
                        // query transition probability average.
                        type: "line",
                        xValueType: "dateTime",
                        showInLegend: true,
                        name: "Query Transition Prob Avg.",
                        dataPoints: qctnQueryTransitionProvAvg
                    },
//                    {
//                        // query probability.
//                        type: "line",
//                        xValueType: "dateTime",
//                        showInLegend: true,
//                        name: "Query Prob.",
//                        dataPoints: qctnQueryProb
//                    },
                    {
                        // query probability last five average.
                        type: "line",
                        xValueType: "dateTime",
                        showInLegend: true,
                        name: "Query Prob Last Five Avg.",
                        dataPoints: qctnQueryProbLastFiveAvg
                    },
                    {
                        // query probability average.
                        type: "line",
                        xValueType: "dateTime",
                        showInLegend: true,
                        name: "Query Prob Avg.",
                        dataPoints: qctnQueryProbAvg
                    }
                ]
            });

            var updateInterval = 1000;
            qctnStartTime = new Date();


            var updateQctnChart = function (count) {
                count = count || 1;
                var qctnTempTime;
                var currentLength = qctnQueryProb.length;
                if ( currentLength > maxDisplayedElements) {
                    var totalSlicingNumber = currentLength - maxDisplayedElements;
                    qctnQueryProb.splice(0, totalSlicingNumber);
                    qctnQueryProbAvg.splice(0, totalSlicingNumber);
                    qctnQueryProbLastFiveAvg.splice(0, totalSlicingNumber);
                    qctnQueryTransitionProb.splice(0, totalSlicingNumber);
                    qctnQueryTransitionProvAvg.splice(0, totalSlicingNumber);
                    qctnQueryTransitionProbLastFiveAvg.splice(0, totalSlicingNumber);
                }

                $.getJSON('../api/qctn/pbqlogs', {
                    start: qctnStartTime.getTime(),
                    end: new Date().getTime(),
                    userName: 'user1',
                    ajax: 'true'
                }, function (data) {
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        qctnTempTime = new Date(data[i].loggingTime);
                        qctnQueryTransitionProbSum += data[i].queryTransitionProbability;
                        qctnQueryProbSum += data[i].queryProbability;
                        totalCount += 1;
                        queryTransitionProbY = data[i].queryTransitionProbability;
                        queryProbY = data[i].queryProbability;

                        // push the remaining query transition probabilities into the chart from the json array.
                        qctnQueryTransitionProb.push({
                            x: qctnTempTime,
                            y: queryTransitionProbY
                        });
                        var queryTransitionProbLastFiveSumTemp = 0;
                        for (var j = qctnQueryTransitionProb.length - 5; j < qctnQueryTransitionProb.length; j++) {
                            queryTransitionProbLastFiveSumTemp += qctnQueryTransitionProb[j].y;
                        }
                        // push the last five average of query transition probabilities.
                        qctnQueryTransitionProbLastFiveAvg.push({
                            x: qctnTempTime,
                            y: queryTransitionProbLastFiveSumTemp / 5
                        })
                        // push the average of query transition probabilities.
                        qctnQueryTransitionProvAvg.push({
                            x: qctnTempTime,
                            y: qctnQueryTransitionProbSum / totalCount
                        })

                        // push the remaining query probabilities into the chart from the json array.
                        qctnQueryProb.push({
                            x: qctnTempTime,
                            y: queryProbY
                        });
                        var queryProbLastFiveSumTemp = 0;
                        for (var j = qctnQueryProb.length - 5; j < qctnQueryProb.length; j++) {
                            queryProbLastFiveSumTemp += qctnQueryProb[j].y;
                        }
                        // push the last five average of query probabilities.
                        qctnQueryProbLastFiveAvg.push({
                            x: qctnTempTime,
                            y: queryProbLastFiveSumTemp / 5
                        })
                        // push the average of query probabilities.
                        qctnQueryProbAvg.push({
                            x: qctnTempTime,
                            y: qctnQueryProbSum / totalCount
                        })
                    }
                    if (qctnTempTime != null) {
                        qctnStartTime = qctnTempTime;
                    }
                });

                // updating legend text with  updated with y Value
//                qctnChart.options.data[0].legendText = "Query Transition Prob.";
                qctnChart.options.data[0].legendText = "Query Transition Prob Last Five Avg.";
                qctnChart.options.data[1].legendText = "Query Transition Prob Avg.";
//                qctnChart.options.data[3].legendText = "Query Prob.";
                qctnChart.options.data[2].legendText = "Query Prob Last Five Avg.";
                qctnChart.options.data[3].legendText = "Query Prob Avg.";
                qctnChart.render();

            };

            // generates first set of dataPoints
            updateQctnChart();

            // update chart after specified interval
            setInterval(function () {
                updateQctnChart()
            }, updateInterval);
        }
    </script>
    <script src="${pageScope.canvasjs}"></script>
    <script src="${pageScope.jqueryUrl}"></script>
</head>
<body>
<%--<h2>${message}</h2>--%>
<a href="../orcn">ORCN Query Probabilities Time Series Graph</a>

<div id="qctnChartContainer" style="height: 300px; width: 100%;"/>

</body>
</html>