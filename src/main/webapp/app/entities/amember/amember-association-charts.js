'use strict';
//初始化各个图表
initCollegePie();
initMajorPie();
//绑定各个查询按钮事件
$('#assoChartBtn').on('click',function () {
    initCollegePie();
});
$('#assoChartBtn2').on('click',function () {
    initMajorPie();
});


// 初始化学院分布饼图
function initCollegePie() {
    var assoId = $('#getSelectedAssoId').html();
    var assoName = $('#getSelectedAssoName').html();
    if (isNaN(assoId)) {
        assoId = '';
    };
    if(assoName =="{{selectAsso.selected.assoName}}"){
        assoName = "全部社团";
    }
    $.ajax({
        type: 'get',
        url:"/api/allAmembersCollegePie/"+assoId,
        dataType:'json',
        success:function(pieData){
            var collegeName = [];
            for(var tmpData in pieData) {
               collegeName.push(pieData[tmpData].name);
           }
            var myChart = echarts.init(document.getElementById('collegePieContainer'));
             var option = {
                title : {
                        text: assoName+'成员学院分布',
                        subtext: '包含医学院',
                        x: 'center',
                        y: '5%'
                    },
                    tooltip : {
                           trigger: 'item',
                           formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            magicType : {
                                show: true, 
                                type: ['pie', 'funnel']
                            },
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    legend: {
                        orient : 'vertical',
                        x : '10%',
                        y : '18%',
                        // data: ['财经学院','电气学院','管理学院','机械学院','计通学院','汽车学院','社科学院','生化学院','体育学院','土建学院','外语学院','医学院','艺术学院','职教学院']
                        data: collegeName
                    },
                    series : [
                        {
                            name: '学院分布',
                            type: 'pie',
                            radius : [20, 140],
                            center: ['50%', '55%'],
                            roseType : 'area',
                            x: '50%',               // for funnel
                            y: '20%',
                            max: 1,               // for funnel
                            width: '10%',		 // for funnel
                            height: '50%',
                            sort : 'ascending',     // for funnel
                            data: pieData,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
            };
            myChart.setOption(option);
        }
    });
 }

// 初始化专业饼图
function initMajorPie() {
    var assoId = $('#getSelectedAssoId2').html();
    var assoName = $('#getSelectedAssoName2').html();
    if (isNaN(assoId)) {
        assoId = '';
    };
    if(assoName =="{{selectAsso.selected2.assoName}}"){
        assoName = "全部社团";
    }
    $.ajax({
        type: 'get',
        url:"/api/allAmembersMajorPie/"+assoId,
        dataType:'json',
        success:function(pieData){
            var collegeName = [];
            for(var tmpData in pieData) {
               collegeName.push(pieData[tmpData].name);
           }
            var myChart = echarts.init(document.getElementById('majorPieContainer'));
              var option = {
                 title : {
                         text: assoName+'成员专业分布',
                         subtext: '包含医学院',
                         x: 'center',
                         y: '5%'
                     },
                     tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                     },
                     toolbox: {
                         show : true,
                         feature : {
                             mark : {show: true},
                             dataView : {show: true, readOnly: false},
                             magicType : {
                                 show: true, 
                                 type: ['pie', 'funnel']
                             },
                             restore : {show: true},
                             saveAsImage : {show: true}
                         }
                     },
                     calculable : true,
                     legend: {
                         orient : 'vertical',
                         x : '10%',
                         y : '18%',
                         // data: ['财经学院','电气学院','管理学院','机械学院','计通学院','汽车学院','社科学院','生化学院','体育学院','土建学院','外语学院','医学院','艺术学院','职教学院']
                         data: collegeName
                     },
                     series : [
                         {
                             name: '专业分布',
                             type: 'pie',
                             radius : [20, 140],
                             center: ['50%', '55%'],
                             roseType : 'area',
                             x: '50%',               // for funnel
                             y: '20%',
                             max: 1,               // for funnel
                             width: '10%',		 // for funnel
                             height: '50%',
                             sort : 'ascending',     // for funnel
                             data: pieData,
                             itemStyle: {
                                 emphasis: {
                                     shadowBlur: 10,
                                     shadowOffsetX: 0,
                                     shadowColor: 'rgba(0, 0, 0, 0.5)'
                                 }
                             }
                         }
                     ]
             };
            myChart.setOption(option);
        }
    });
}
