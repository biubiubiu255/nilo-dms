<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%--<%@ include file="../common/header.jsp" %>--%>
<link rel="stylesheet" href="${ctx}/dist/css/dm-notif-style.css">
<body class="hold-transition skin-blue sidebar-mini fixed">



<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="renderer" content="webkit">
  		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>DMS管理面板</title>
		<link rel="stylesheet" type="text/css" href="../../layui/css/layui.css"/>
		<link rel="stylesheet" type="text/css" href="../../layui/css/admin.css"/>


	</head>
	<body>
		<div class="wrap-container welcome-container">
			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend id="monthDesc"></legend>
			</fieldset>
			<div class="row">
				<div class="welcome-left-container col-lg-12">
					<div class="data-show">
						<ul class="clearfix">
							<li class="col-sm-12 col-md-4 col-xs-12">
								<a href="javascript:;" class="clearfix">
									<div class="icon-bg bg-blue f-l">
										<span class="iconfont">&#xe639;</span>
									</div>
									<div class="right-text-con">
										<p class="name">Aug total arrived</p>
										<p><span class="color-blue" id="arrivedMonth"></span></p>
									</div>
								</a>
							</li>
							<li class="col-sm-12 col-md-4 col-xs-12">
								<a href="javascript:;" class="clearfix">
									<div class="icon-bg bg-org f-l">
										<span class="iconfont">&#xe602;</span>
									</div>
									<div class="right-text-con">
										<p class="name">Aug total signed</p>
										<p><span class="color-org" id="signedMonth"></span></p>
									</div>
								</a>
							</li>
							<li class="col-sm-12 col-md-4 col-xs-12">
								<a href="javascript:;" class="clearfix">
									<div class="icon-bg bg-blue f-l">
										<span class="iconfont">&#xe639;</span>
									</div>
									<div class="right-text-con">
										<p class="name">Aug total delayed(arrived more than 3 days but not been signed)</p>
										<p><span class="color-blue" id="timeoutSignedMonth"></span></p>
									</div>
								</a>
							</li>


						</ul>
					</div>
					<!--图表-->
					<div class="chart-panel panel panel-default">
						<div class="panel-body" id="chart" style="height: 376px;">
						</div>
					</div>
					<!--服务器信息-->

				</div>

			</div>
		</div>
		<script src="../../layui/layui.js" type="text/javascript" charset="utf-8"></script>
		<%--<script src="../../plugins/echarts/echarts.js"></script>--%>
		<script src="../../plugins/echarts/echarts.common.min.js"></script>
		<script type="text/javascript">

            function haveMonthTail(month){with(new Date()){setMonth(month-1);setDate(31);return getDate()>30;}}

			layui.use(['layer','jquery','util'], function(){
				var layer 	= layui.layer;
				var $=layui.jquery;
				var u=layui.util;
				$("#monthDesc").html(new Date().toDateString().split(" ")[1]);

                var myChart = echarts.init(document.getElementById('chart'));
                var colors = ['#5793f3', '#d14a61', '#675bba'];

                $.post('/penal_data/signedMonth.html', {}, function (data) {
                    $("#signedMonth").html(data.data.count);
                }, "json");
                $.post('/penal_data/outTimeSigned.html', {timeOutNumber: 3}, function (data) {
                    $("#timeoutSignedMonth").html(data.data.count);
                }, "json");
                $.post('/penal_data/arrivedMonth.html', {}, function (data) {
                    $("#arrivedMonth").html(data.data.count);
                }, "json");
                var toTime  = parseInt(new Date().getTime()/1000);
                var fromTime= toTime-86400*30;
                $.post('/penal_data/signedMonthGroup.html', {toTime:toTime,fromTime:fromTime}, function (data) {
                    //console.log("分组数据");
                    //console.log(data);
                    var n  = 30;  		 //份数
                    var portion = 30/n;  //份额大小
                    var fillArr = new Array();
                    var xAxisArr= new Array();

					var startDate = new Date(toTime*1000).getDate();
					var endMonth = haveMonthTail(new Date(toTime*1000).getMonth()) ? 31 : 30;

                    for (var i=0;i<endMonth;i++){
                        var d = startDate+i<=endMonth ? startDate+i : i+startDate-endMonth;
                        xAxisArr.push(d);
					}

                    for (var i=0;i<xAxisArr.length;i++){
                        var mk = xAxisArr[i]>9 ? xAxisArr[i]+"" : "0"+xAxisArr[i];
                        //console.log(mk, i%portion);
                        fillArr.push(typeof(data.data[mk])=='undefined' ? 0 : data.data[mk].count);
						/*
						if(i%portion==0 || i==1){
                            //xAxisArr.push(i);
                            fillArr.push(typeof(data.data[mk])=='undefined' ? 0 : data.data[mk].count);
                        }*/
                    }


                    console.log(fillArr, endMonth, xAxisArr.length);


                    var option = {
                        color: colors,
                        title: {text:'Sign chart',
                            x:'center',
                            y:'top',
                            textAlign:'left'
                        },
                        //悬浮位置提示
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'cross'
                            }
                        },
                        grid: {
                            right: '20%'
                        },
                        toolbox: {
                            feature: {
                                dataView: {show: true, readOnly: false, title:'dataView'},
                                restore: {show: true, title:'restore'},
                                saveAsImage: {show: true, title:'saveAsImage'}
                            }
                        },

                        xAxis: [
                            {
                                type: 'category',
                                axisTick: {
                                    alignWithLabel: true
                                },
                                data: xAxisArr
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value',
                                name: 'Sign Number',
                                position: 'left',
                                axisLine: {
                                    lineStyle: {
                                        color: colors[0]
                                    }
                                },
                                axisLabel: {
                                    formatter: '{value} pieces'
                                }
                            }
                        ],
                        series: [
                            {
                                name:'Sign Number',
                                type:'bar',
                                data:fillArr
                            }
                        ]
                    };

                    myChart.setOption(option);

                    //myChart.resize();

                }, "json");



                /*

                                //图表
                                var myChart = echarts.init(document.getElementById('chart'));
                                //蓝色  红色  紫色
                                var colors = ['#5793f3', '#d14a61', '#675bba'];






                               var option = {
                                    color: colors,
                                    title: {text:'Sign chart',
                                            x:'center',
                                            y:'top',
                                            textAlign:'left'
                                            },
                                    tooltip: {
                                        trigger: 'axis',
                                        axisPointer: {
                                            type: 'cross'
                                        }
                                    },
                                    grid: {
                                        right: '20%'
                                    },
                                    toolbox: {
                                        feature: {
                                            dataView: {show: true, readOnly: false},
                                            restore: {show: true},
                                            saveAsImage: {show: true}
                                        }
                                    },
                                    xAxis: [
                                        {
                                            type: 'category',
                                            axisTick: {
                                                alignWithLabel: true
                                            },
                                            data: ['1号','5号','10号','15号','20号','25号','30号']
                                        }
                                    ],
                                    yAxis: [
                                        {
                                            type: 'value',
                                            name: 'Sign Number',
                                            position: 'right',
                                            axisLine: {
                                                lineStyle: {
                                                    color: colors[0]
                                                }
                                            },
                                            axisLabel: {
                                                formatter: '{value} number'
                                            }
                                        },
                                        {
                                            type: 'value',
                                            name: 'Sign',
                                            position: 'left',
                                            axisLine: {
                                                lineStyle: {
                                                    color: colors[1]
                                                }
                                            },
                                            axisLabel: {
                                                formatter: '{value} number'
                                            }
                                        }




                                    ],
                                    series: [
                                        {
                                            name:'Sign Number',
                                            type:'bar',
                                            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6]
                                        },
                                        {
                                            name:'Sign',
                                            type:'line',
                                            //yAxisIndex: 1,
                                            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6]
                                        }
                                    ]
                                };

               var option = {
                   color: colors,
                   title: {text:'Sign chart',
                       x:'center',
                       y:'top',
                       textAlign:'left'
                   },
				    //悬浮位置提示
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        }
                    },
                    grid: {
                        right: '20%'
                    },
                    toolbox: {
                        feature: {
                            dataView: {show: true, readOnly: false},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },

                    xAxis: [
                        {
                            type: 'category',
                            axisTick: {
                                alignWithLabel: true
                            },
                            data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            name: '蒸发量',
                            position: 'right',
                            axisLine: {
                                lineStyle: {
                                    color: colors[0]
                                }
                            },
                            axisLabel: {
                                formatter: '{value} ml'
                            }
                        },

                        {
                            type: 'value',
                            name: '温度',
                            position: 'left',
                            axisLine: {
                                lineStyle: {
                                    color: colors[2]
                                }
                            },
                            axisLabel: {
                                formatter: '{value} °C'
                            }
                        }
                    ],
                    series: [
                        {
                            name:'蒸发量23',
                            type:'bar',
                            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
                        },
                        {
                            name:'降水量123',
                            type:'line',
                            yAxisIndex: 1,  //作用是按照哪个Y轴
                            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
                        }
                    ]
                };*/


                // 使用刚指定的配置项和数据显示图表。
                //myChart.setOption(option);
                //addTabs({id:'601',title: 'Pickup Report',close: true,url: 'report/pick_up/listPage.html'});
				$(window).resize(function(){
					//myChart.resize();

				})
			});
		</script>
	</body>
</html>
