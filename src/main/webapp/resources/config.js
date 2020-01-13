var parents = null;
var subMenus = {
    /*三级结构菜单*/
    /*'sub-report': [
        {
            dtId: '-88-',
            dtname: '总览',
            menuId: 'sub-overAll',
            menuColor: '#0091FE',
            value: [
                {
                    ddId: '-89-',
                    ddname: '质量报表',
                    ddflag: 'zonglan',
                    ddpath: 'projectOverview.html',
                    menuId: 'sub-qulaityReport'
                }
            ]
        },
        {
            dtId: '-13-',
            dtname: '质量总览',
            menuId: 'sub-qualityAll',
            value: [
                {ddId: '-15-', ddname: '质量总览', ddpath: 'starPage.html', menuId: 'sub-qualityAll-01'}
            ]
        },
        {
            dtId: '-14-',
            dtname: '团队总览',
            menuId: 'sub-teamAll',
            value: [
                {ddId: '-16-', ddname: '关键角色', ddpath: 'teamShow.html', menuId: 'sub-keyRole'}
            ]
        },
        {
            dtId: '-17-',
            dtname: '进度总览',
            menuId: 'sub-progressAll',
            value: [
                {ddId: '-19-', ddname: '进度预警', ddpath: 'scheduleShow.html', menuId: 'sub-progress-warn'}
            ]
        },
        {
            dtId: '-20-',
            dtname: '质量PCB',
            menuId: 'sub-qualityPcb',
            value: [
                {ddId: '-23-', ddname: '开发过程质量', ddpath: 'pcbKaifaGuocheng.html', menuId: 'sub-quality-develop'},
                {ddId: '-24-', ddname: '测试过程质量', ddpath: 'pcbCeshiGuocheng.html', menuId: 'sub-quality-test'},
                {ddId: '-25-', ddname: '工程能力', ddpath: 'pcbGongcheng.html', menuId: 'sub-process-bality'},
                {ddId: '-26-', ddname: '代码质量', ddpath: 'pcbDaima.html', menuId: 'sub-code-qality'}
            ]
        },
        {
            dtId: '-21-',
            dtname: '效率PCB',
            menuId: 'sub-effect-pcb',
            value: [
                {ddId: '-27-', ddname: '研发效率', ddpath: 'pcbYanfa.html', menuId: 'sub-develop-effect'},
                {ddId: '-28-', ddname: '迭代效率', ddpath: 'pcbCeshi.html', menuId: 'sub-ite-effect'}
            ]
        },
        {
            dtId: '-22-',
            dtname: '月报',
            menuId: 'sub-month-report',
            value: [
                {ddId: '-29-', ddname: '低产出分析', ddpath: 'reportForm.html', menuId: 'sub-low-product'},
                {ddId: '-30-', ddname: '效率看板', ddpath: 'qualityModel.html', menuId: 'sub-effect-board'},
                {ddId: '-31-', ddname: '质量看板', ddpath: 'statistical.html', menuId: 'sub-quality-board'}
                /!*{ddname :'研发效率6+1',ddpath :'sixAddOneHomePage.html',},*!/
            ]
        }
    ],*/

    //二级菜单
    'sub-system': [
        {
            ddId:'-36-',
            ddname :'角色管理',
            ddpath :'permissionManage.html',
            menuId:'sub-sysRole',
            menuColor:'#0091FE'
        },
        {
            ddId:'-37-',
            ddname :'用户管理',
            ddpath :'sysUserManage.html',
            menuId:'sub-sysUser'
        },
        /*{
            ddId:'-34-',
            ddname :'客户管理',
            ddpath :'customerManage.html',
            menuId:'sub-custom'
        },
        {
            ddId:'-35-',
            ddname :'问题反馈',
            ddpath :'questionAndProblem.html',
            menuId:'sub-question'
        },
        {
            ddId:'-77-',
            ddname :'数据字典',
            ddpath :'sysDict.html',
            menuId:'sub-dict'
        },
        {
            ddId:'-98-',
            ddname :'数据管理',
            ddpath :'dataManagement.html',
            menuId:'sub-dataManager'
        },*/
        {
            ddId:'-38-',
            ddname :'日志管理',
            ddpath :'projectOperationClone.html',
            menuId:'sub-logging'
        },
        {
            ddId:'-39-',
            ddname :'消息中心',
            ddpath :'messageTipsClone.html',
            menuId:'sub-message'
        },
        {
            ddId:'-40-',
            ddname :'各类后台配置',
            ddpath :'sysDictClone.html',
            menuId:'sub-configuration'
        }
    ],
    'sub-work': [//condition:1-显示
        // {
        //     ddId:'-40-',
        //     ddname :'基本配置',
        //     ddpath :'basic.html',
        //     menuId:'sub-basic',
        //     menuColor:'#0091FE'
        // },
        {
            ddId: '-10-',
            ddname: '迭代计划',
            ddpath: 'iterationworkNew.html',
            menuId: 'sub-iterationwork',
            condition: 1
        },
        {
            ddId: '-11-',
            ddname: '需求配置',
            ddpath: 'demandworkNew.html',
            menuId: 'sub-demandwork',
            condition:1
        },
        {
            ddId: '-12-',
            ddname: '交付件配置',
            ddpath: 'deliver.html',
            menuId: 'sub-deliver',
            condition:1
        },
        {
            ddId: '-13-',
            ddname: '人员配置',
            ddpath: 'personnelManagement.html',
            menuId: 'sub-personnelManagement',
            condition:1
        },
        {
            ddId: '-14-',
            ddname: '工具配置',
            ddpath: 'tool.html',
            menuId: 'sub-tool'
            // condition:1
        },
        {
            ddId: '-15-',
            ddname: '----------',
            menuId: 'sub-line'
        },
        {
            ddId: '-16-',
            ddname: '风险问题',
            ddpath: 'risk.html',
            menuId: 'sub-risk',
            condition:1
        },
        {
            ddId: '-17-',
            ddname: '度量表配置',
            ddpath: 'report/metricsTableConfig.html',
            menuId: 'sub-metricsTableConfig'
        },
        {
            ddId: '-18-',
            ddname: '指标管理',
            ddpath: 'report/kpiManagement.html',
            menuId: 'sub-kpiManagement'
        },
        {
            ddId: '-19-',
            ddname: '报表配置',
            ddpath: 'report/reportManagement.html',
            menuId: 'sub-customization'
        },
        {
            ddId: '-20-',
            ddname: '项目裁剪',
            ddpath: 'report/reportConfigExcluded.html',
            menuId: 'sub-peoject',
            condition: 1
        },
        {
            ddId: '-21-',
            ddname: '采集配置',
            ddpath: 'collection.html',
            menuId: 'sub-collection',
            condition: 1
        },
        {
            ddId: '-22-',
            ddname: '报表审核',
            ddpath: 'report/reportChecks.html',
            menuId: 'sub-reportChecks'
        },
        {
            ddId: '-23-',
            ddname: '度量表管理',
            ddpath: 'report/metricsTableManagement.html',
            menuId: 'sub-metricsTableManagement',
            condition: 1
        },
        {
            ddId: '-24-',
            ddname: '报表管理',
            ddpath: 'report/reportDataManagement.html',
            menuId: 'sub-reportDataManagement',
            condition: 1
        },
        {
            ddId: '-87-',
            ddname: '采集接口',
            ddpath: 'collection/interface.html',
            menuId: 'sub-interface1',
            condition: 1
        },
        {
            ddId: '-88-',
            ddname: '采集任务',
            ddpath: 'collection/task.html',
            menuId: 'sub-interface4',
            condition: 1
        },
        {
            ddId: '-89-',
            ddname: '进行中',
            ddpath: 'collection/task_instance-runing.html',
            menuId: 'sub-interface3',
            condition: 1
        },
        {
            ddId: '-90-',
            ddname: '已完成',
            ddpath: 'collection/task_instance-done.html',
            menuId: 'sub-interface2',
            condition: 1
        }
    ],
    'sub-project': [
        {
            ddId: '-998-',
            ddname: '树形',
            ddpath: 'projectManagement.html?queryCriteria=1',
            menuId: 'sub-proTree',
            menuColor: '#0091FE'
        },
        {
            ddId: '-999-',
            ddname: '列表',
            ddpath: 'projectManagement.html',
            menuId: 'sub-proList'
        }
    ],

    /*项目详情*/
    'sub-projectDetails': [
        {
            ddId: '-48-',
            ddname: '基本信息',
            ddpath: 'gaikuang.html',
            menuId: 'sub-basicInformation',
            menuColor: '#0091FE'
        },
        {
            ddId: '-49-',
            ddname: '进度',
            ddpath: 'projectIteration.html',
            menuId: 'sub-progress'
        },
        {
            ddId: '-50-',
            ddname: '质量',
            ddpath: 'measureCommentTree.html',
            menuId: 'sub-details-quality'
        },
        {
            dtId: '-51-',
            dtname: '范围',
            menuId: 'sub-details-range',
            value: [
                {ddId: '-59-', ddname: '需求', ddpath: 'projectDemand.html', menuId: 'sub-report-demand'},
                {ddId: '-60-', ddname: '工作量', ddpath: 'gaikuang.html', menuId: 'sub-report-workload'}
            ]
        },
        {
            dtId: '-52-',
            dtname: '资源',
            menuId: 'sub-resources',
            value: [
                {ddId: '-61-', ddname: '团队成员', ddpath: 'projectTeamMembers.html', menuId: 'sub-report-team'},
                {ddId: '-62-', ddname: '关键角色', ddpath: 'projectKeyRole.html', menuId: 'sub-report-keyRole'},
                {ddId: '-85-', ddname: '问题成员', ddpath: 'projectProblemMember.html', menuId: 'sub-report-problem'},
                {ddId: '-86-', ddname: '出勤', ddpath: 'projectAttendance.html', menuId: 'sub-report-attendance'}
            ]
        },
        {
            dtId: '-53-',
            dtname: '效率',
            menuId: 'sub-efficiency',
            value: [
                {ddId: '-63-', ddname: '代码产出', ddpath: 'codeOutPut.html', menuId: 'sub-report-output'},
                {ddId: '-64-', ddname: '测试用例', ddpath: 'testCase.html', menuId: 'sub-report-testCase'},
                {ddId: '-65-', ddname: '文档文件', ddpath: 'document.html', menuId: 'sub-report-documents'}
            ]
        },
        {
            dtId: '-54-',
            dtname: '报表',
            menuId: 'sub-details-report',
            value: [
                {ddId: '-66-', ddname: '过程跟踪', ddpath: 'processTrack.html', menuId: 'sub-report-process'},
                {ddId: '-67-', ddname: '验收跟踪', ddpath: 'projectInspect.html', menuId: 'sub-report-acceptance'},
                {ddId: '-68-', ddname: '人员跟踪', ddpath: 'personnelTrack.html', menuId: 'sub-report-people'},
                {ddId: '-69-', ddname: '问题跟踪', ddpath: 'problemDscription.html', menuId: 'sub-report-problem'},
                {ddId: '-70-', ddname: '可信跟踪', ddpath: 'developIndex.html', menuId: 'sub-report-credible'},
                {ddId: '-71-', ddname: '自定义报表', ddpath: 'custom.html', menuId: 'sub-report-custom'}
            ]
        },
        {
            dtId: '-55-',
            dtname: '配置',
            menuId: 'sub-configuration',
            value: [
                {ddId: '-72-', ddname: '交付', ddpath: 'deliver.html', menuId: 'sub-deliver'},
                {ddId: '-73-', ddname: '人员', ddpath: 'personnelManagement.html', menuId: 'sub-personnelManagement'},
                {ddId: '-74-', ddname: '工具', ddpath: 'tool.html', menuId: 'sub-tool'}
            ]
        }
    ],
    /*看板*/
    'sub-board': [
        {
            dtId: '-28-',
            dtname: '组织看板',
            menuId: 'sub-group-board',
            value: [
                {ddId: '-28-', ddname: '项目群总览', ddpath: 'board/groupOverview.html', menuId: 'sub-report-groupOverview'},
                {ddId: '-28-', ddname: '项目群执行跟踪', ddpath: 'board/groupExecute.html', menuId: 'sub-report-groupExecute'},
                {ddId: '-28-', ddname: '项目群验收跟踪', ddpath: 'board/groupAcceptance.html', menuId: 'sub-report-groupAcceptance'},
                {ddId: '-28-', ddname: '项目群人员跟踪', ddpath: 'board/groupPerson.html', menuId: 'sub-report-groupPerson'},
                {ddId: '-28-', ddname: '项目群问题跟踪', ddpath: 'board/groupIssue.html', menuId: 'sub-report-groupIssue'},
                {ddId: '-28-', ddname: '项目群可信跟踪', ddpath: 'board/groupCredible.html', menuId: 'sub-report-groupCredible'}
            ],
            menuColor: '#0091FE'
        },
        {
            dtId: '-29-',
            dtname: '项目看板',
            menuId: 'sub-allItem-board',
            value: [
                {ddId: '-29-', ddname: '项目执行跟踪', ddpath: 'board/allItemExecute.html', menuId: 'sub-report-allItemExecute'},
                {ddId: '-29-', ddname: '项目验收跟踪', ddpath: 'board/allItemAcceptance.html', menuId: 'sub-report-allItemAcceptance'},
                {ddId: '-29-', ddname: '项目人员跟踪', ddpath: 'board/allItemPerson.html', menuId: 'sub-report-allItemPerson'},
                {ddId: '-29-', ddname: '项目问题跟踪', ddpath: 'board/allItemIssue.html', menuId: 'sub-report-allItemIssue'},
                {ddId: '-29-', ddname: '项目可信跟踪', ddpath: 'board/allItemCredible.html', menuId: 'sub-report-allItemCredible'}
            ]
        }
    ]
};

