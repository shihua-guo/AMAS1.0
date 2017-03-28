'use strict';
//初始化各个图表
initGenderPie();
initGradePie();
$('#assoChartBtn3').on('click',function () {
    initGenderPie();
});
$('#assoChartBtn4').on('click',function () {
    initGradePie();
});

// 初始化性别饼图
function initGenderPie() {
    var assoId = $('#getSelectedAssoId3').html();
    var assoName = $('#getSelectedAssoName3').html();
    if (isNaN(assoId)) {
        assoId = '';
    };
    if(assoName =="{{selectAsso.selected3.assoName}}"){
        assoName = "全部社团";
    }
    $.ajax({
        type: 'get',
        url:"/api/allAmembersGenderPie/"+assoId,
        dataType:'json',
        success:function(pieData){
            var collegeName = [];
            for(var tmpData in pieData) {
               collegeName.push(pieData[tmpData].name);
           }
            var myChart = echarts.init(document.getElementById('genderPieContainer'));
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

// 初始化年级饼图
function initGradePie() {
    var assoId = $('#getSelectedAssoId4').html();
    var assoName = $('#getSelectedAssoName4').html();
    if (isNaN(assoId)) {
        assoId = '';
    };
    if(assoName =="{{selectAsso.selected4.assoName}}"){
        assoName = "全部社团";
    }
    $.ajax({
        type: 'get',
        url:"/api/allAmembersGradePie/"+assoId,
        dataType:'json',
        success:function(pieData){
            var collegeName = [];
            for(var tmpData in pieData) {
               collegeName.push(pieData[tmpData].name);
           }
            var myChart = echarts.init(document.getElementById('gradePieContainer'));
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