
////首页
////正在运行的节点
//const URL_SYS1 = "/pages/sys1.json"
////正在运行的任务
//const URL_SYS2 = "/pages/sys2.json"
////集群文件系统信息
//const URL_SYS3 = "/pages/sys3.json"
//
////添加任务
////提交任务
//const  URL_TASK_SUB = "/test"
////获取节点列表
//const  URL_TASK_GET_NODES="/pages/addselect.json"
////获取节点的文件夹，树形结构使用
//const  URL_TASK_FOLDER="/pages/folder.json"
//
////任务详情
////修改任务
//const URL_TASK_MODIFY="/teup"
////详细信息-系统消息
//const URL_TASK_DETAIL_MSG="/pages/detailmsg.json"
////详细信息-任务详细信息
//const URL_TASK_DETAIL="/pages/detail2.json"
//const URL_TASK_DELE="/pages/folder.json"
//const URL_TASK_PAUSE="/pages/folder.json"
//const URL_TASK_START="/pages/folder.json"
//
////历史页面
//const URL_TASK_HIS="/pages/sys2.json"
//const URL_TASK_HIS_DETAIL="/pages/detail2.json"

//////////////////////////////以上为测试用，正式服务器要注释掉，并开启下面的地址////////////////////////


 const SERVER_URL="http://localhost:8001"
 const URL_SYS1 = SERVER_URL+"/getEurekaServices"
 const URL_SYS2 = SERVER_URL+"/getAllTasks"
 const URL_SYS3 = SERVER_URL+"/getLastTenInfo"
 const URL_TASK_SUB = SERVER_URL+"/addTask"
 const URL_TASK_GET_NODES=SERVER_URL+"/getSlaveNodeServices"
 const URL_TASK_FOLDER=SERVER_URL+"/list"
 const URL_TASK_MODIFY=SERVER_URL+"/modifyTask"
 const URL_TASK_DETAIL_MSG=SERVER_URL+"/getTaskInformation"
 const URL_TASK_DETAIL=SERVER_URL+"/getTaskDetails"
 const URL_TASK_DELE=SERVER_URL+"/deleteTask"
 const URL_TASK_PAUSE=SERVER_URL+"/pauseTask"
 const URL_TASK_START=SERVER_URL+"/startTask"
 const URL_TASK_HIS=SERVER_URL+"/getAllHistoryTasks"
 const URL_TASK_HIS_DETAIL=SERVER_URL+"/getHistoryTaskDetails"
 const URL_TASK_PROGRESS=SERVER_URL+"/getTaskProgress"