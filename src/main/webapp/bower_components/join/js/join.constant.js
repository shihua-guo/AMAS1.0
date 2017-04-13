'use strict';  
'use strict';
angular.module('joinApp', ['ngRoute','ngResource']);
angular.module('joinApp').constant('CityData', [  
  {  
      label: '财经学院',
      value: 'FINANCE',  
      majors: [  
        {  
          value: '财务管理'  
        },  
        {  
          value: '国际经济与贸易'  
        },  
        {  
          value: '国际商务'  
        },  
        {  
          value: '会计学'  
        },  
        {  
          value: '金融学'  
        },  
        {  
          value: '经济学'  
        }  
      ]  
    },  
    {  
      label: '电气学院',
      value: 'ELECTRICAL',  
      majors: [  
        {  
          value: '测控技术与仪器'  
        },  
        {  
          value: '电气工程及其自动化'  
        },  
        {  
          value: '电气工程与自动化'  
        },
        {  
          value: '电子科学与技术'  
        },
        {  
          value: '电子信息工程'  
        },
        {  
          value: '电子信息科学与技术'  
        },
        {  
          value: '自动化'  
        },
        {  
          value: '自动化(卓越工程师)'  
        }  
      ]  
    },  
    {  
      label: '管理学院',
      value: 'MANAGEMENT',  
      majors: [  
        {  
          value: '工商管理'  
        },  
        {  
          value: '工业工程'  
        },  
        {  
          value: '公共事业管理'  
        },
        {  
          value: '人力资源管理'  
        },
        {  
          value: '市场营销'  
        },
        {  
          value: '物流管理'  
        },
        {  
          value: '信息管理与信息系统'  
        } 
      ]  
    },  
    {  
      label: '机械学院',
      value: 'MECHANICAL',  
      majors: [  
        {  
          value: '工业设计'  
        },  
        {  
          value: '机械电子工程'  
        },  
        {  
          value: '机械工程'  
        },
        {  
          value: '机械工程及自动化(模具设计与制造)'  
        },
        {  
          value: '机械工程及自动化(数控技术方向)'  
        },
        {  
          value: '机械工程及自动化(卓越工程师)'  
        }  
      ]  
    },  
    {  
      label: '计通学院',
      value: 'COMPUTER',  
      majors: [  
        {  
          value: '计算机科学与技术(软件工程方向)'  
        },  
        {  
          value: '计算机科学与技术(卓越工程师)'  
        },  
        {  
          value: '软件工程'  
        },
        {  
          value: '数字媒体技术'  
        },
        {  
          value: '通信工程'  
        },
        {  
          value: '物联网工程'  
        }  
      ]  
    },  
    {  
      label: '理学院',
      value: 'SCIENCE',  
      majors: [  
        {  
          value: '数学与应用数学'  
        },  
        {  
          value: '统计学'  
        },  
        {  
          value: '信息与计算科学'  
        },
        {  
          value: '应用统计学'  
        }  
      ]  
    },  
    {  
      label: '汽车学院',
      value: 'AUTOMOTIVE',  
      majors: [  
        {  
          value: '车辆工程'  
        },  
        {  
          value: '工程力学'  
        },  
        {  
          value: '交通运输'  
        },
        {  
          value: '交通运输(汽车电子技术与检测诊断)'  
        },
        {  
          value: '交通运输(卓越工程师)'  
        },
        {  
          value: '汽车服务工程'  
        },
        {  
          value: '物流工程'  
        }  
      ]  
    },  
    {  
      label: '社科学院',
      value: 'SOCIAL',  
      majors: [  
        {  
          value: '社会工作'  
        }  
      ]  
    },  
    {  
      label: '生化学院',
      value: 'BIOCHEMISTRY',  
      majors: [  
        {  
          value: '纺织工程'  
        },  
        {  
          value: '化学工程与工艺'  
        },  
        {  
          value: '化学工程与工艺(卓越工程师)'  
        },
        {  
          value: '生物工程'  
        },
        {  
          value: '食品科学与工程'  
        },
        {  
          value: '应用化学'  
        },
        {  
          value: '制药工程'  
        }  
      ]  
    },  
    {  
      label: '体育学院',
      value: 'PHYSICAL',  
      majors: [  
        {  
          value: '社会体育'  
        },  
        {  
          value: '社会体育指导与管理'  
        }  
      ]  
    },  
    {  
      label: '土建学院',
      value: 'CIVIL',  
      majors: [  
        {  
          value: '工程管理'  
        },  
        {  
          value: '工程造价'  
        },  
        {  
          value: '建筑学'  
        },
        {  
          value: '土木工程'  
        },
        {  
          value: '土木工程(交通土建方向)'  
        },
        {  
          value: '土木工程(卓越工程师)'  
        }  
      ]  
    },  
    {  
      label: '外语学院',
      value: 'FOREIGN',  
      majors: [  
        {  
          value: '英语'  
        },  
        {  
          value: '英语(科技翻译方向)'  
        }  
      ]  
    },  
    {  
      label: '医学院',
      value: 'MEDICAL',  
      majors: [  
        {  
          value: '护理学'  
        },  
        {  
          value: '药学'  
        },  
        {  
          value: '医学检验技术'  
        } 
      ]  
    },  
    {  
      label: '艺术学院',
      value: 'ART',  
      majors: [  
        {  
          value: '对外汉语'  
        },  
        {  
          value: '服装设计与工程'  
        },  
        {  
          value: '服装与服饰设计'  
        },
        {  
          value: '汉语国际教育'  
        },
        {  
          value: '环境设计'  
        },
        {  
          value: '视觉传达设计'  
        },
        {  
          value: '艺术设计'  
        },
        {  
          value: '音乐学'  
        }  
      ]  
    },  
    {  
      label: '职教学院',
      value: 'VOCATIONAL',  
      majors: [  
        {  
          value: '交通运输(汽车电子技术与检测诊断)'  
        },  
        {  
          value: '会计学'  
        },  
        {  
          value: '土木工程'  
        },
        {  
          value: '工程管理'  
        },
        {  
          value: '机械工程'  
        },
        {  
          value: '机械工程及自动化(数控技术方向)'  
        },
        {  
          value: '机械工程及自动化(模具设计与制造)'  
        },
        {  
          value: '汽车服务工程'  
        },
        {  
          value: '电子信息工程'  
        },
        {  
          value: '电气工程及其自动化'  
        },
        {  
          value: '车辆工程'  
        }   
      ]  
    }           
]);  