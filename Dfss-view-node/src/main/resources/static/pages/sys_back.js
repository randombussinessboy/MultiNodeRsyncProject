$(function () {
    tableInit();//正在运行的节点
    tableInit2();//正在运行的任务
    $('#tableThree').hide();
    tableInit3();

    //每隔1秒，刷新消息
    setInterval(function () {
        $("#loadDiv").show();
        $('#tableThree').hide();
        $("#tableThree").bootstrapTable('destroy');
        tableInit3()
    },1000*60)
})

//正在运行的节点
function tableInit() {
    $('#tableOne').bootstrapTable({
        url: '/pages/sys1.json', // 请求后台的URL（*）
        // url: 'http://localhost:8001/getEurekaServices', // 请求后台的URL（*）
        method: 'get', // 请求方式（*）
        contentType: "application/x-www-form-urlencoded",//post 必须制定类型，否则不能正常传值
        toolbar: '#toolOne', // 工具按钮用哪个容器
        striped: true, // 是否显示行间隔色
        cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, // 是否显示分页（*）
        sidePagination: "server", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pageSize: 5, // 每页的记录行数（*）
        pageList: [5, 10, 15], // 可供选择的每页的行数（*）
        search: false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
        showColumns: true, // 是否显示所有的列
        showRefresh: true, // 是否显示刷新按钮
        minimumCountColumns: 2, // 最少允许的列数
        clickToSelect: true, // 是否启用点击选中行
        singleSelect: true,//开启单选，默认为多选
        uniqueId: "id", // 每一行的唯一标识，一般为主键列
        showToggle: false, // 是否显示详细视图和列表视图的切换按钮
        cardView: false, // 是否显示详细视图
        detailView: false, // 是否显示父子表
        columns: [ {
            field: 'serviceName',
            title: '服务名称'
        },{
            field: 'url',
            title: '所在节点',
        }]
    });
}


//正在运行的任务
function tableInit2() {
    $('#tableTwo').bootstrapTable({
        url: '/pages/sys2.json', // 请求后台的URL（*）
        // url: 'http://localhost:8001/getAllTasks', // 请求后台的URL（*）
        method: 'get', // 请求方式（*）
        contentType: "application/x-www-form-urlencoded",//post 必须制定类型，否则不能正常传值
        toolbar: '#toolTwo', // 工具按钮用哪个容器
        striped: true, // 是否显示行间隔色
        cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, // 是否显示分页（*）
        sidePagination: "server", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pageSize: 5, // 每页的记录行数（*）
        pageList: [5, 10, 15], // 可供选择的每页的行数（*）
        search: false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
        showColumns: true, // 是否显示所有的列
        showRefresh: true, // 是否显示刷新按钮
        minimumCountColumns: 2, // 最少允许的列数
        clickToSelect: true, // 是否启用点击选中行
        singleSelect: true,//开启单选，默认为多选
        uniqueId: "id", // 每一行的唯一标识，一般为主键列
        showToggle: false, // 是否显示详细视图和列表视图的切换按钮
        cardView: false, // 是否显示详细视图
        detailView: false, // 是否显示父子表
        columns: [ {
            field: 'taskId',
            title: '任务id'
        },{
            field: 'taskType',
            title: '任务类型',
        },{
            field: 'taskState',
            title: '任务状态',
            formatter: function (data, row, index) {
                if(data=="正在运行"){
                    return "<span style='color: green' >"+data+"</span>";
                }else if(data =="暂停中"){
                    return "<span style='color: red' >"+data+"</span>";
                }else{
                    return data;
                }

            }
        },{
            field: 'source',
            title: '同步源节点'
        },{
            field: 'destinations',
            title: ' 同步目的节点',
            formatter: function (data, row, index) {
                var html="";
                for(var i=0;i<data.length;i++){
                    html+=data[i]+"</br>"
                }
                return html;
            }
        }]
    });
}

function tableInit3() {
    $('#tableThree').bootstrapTable({
        url: '/pages/sys3.json', // 请求后台的URL（*）
        method: 'get', // 请求方式（*）
        contentType: "application/x-www-form-urlencoded",//post 必须制定类型，否则不能正常传值
        toolbar: '#toolThree', // 工具按钮用哪个容器
        striped: true, // 是否显示行间隔色
        cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, // 是否显示分页（*）
        sidePagination: "server", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pageSize: 5, // 每页的记录行数（*）
        pageList: [5, 10, 15], // 可供选择的每页的行数（*）
        search: false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
        showColumns: true, // 是否显示所有的列
        showRefresh: true, // 是否显示刷新按钮
        minimumCountColumns: 2, // 最少允许的列数
        clickToSelect: true, // 是否启用点击选中行
        singleSelect: true,//开启单选，默认为多选
        uniqueId: "id", // 每一行的唯一标识，一般为主键列
        showToggle: false, // 是否显示详细视图和列表视图的切换按钮
        cardView: false, // 是否显示详细视图
        detailView: false, // 是否显示父子表
        columns: [ {
            field: 'id',
            title: '序号'
        },{
            field: 'msg',
            title: '消息',
            formatter: function (data, row, index) {
                var html=data.substring(0,data.length-1)
                return html;
            }
        }],
        onLoadSuccess:function (data) {
            $("#loadDiv").hide();
            $('#tableThree').show();
        }
    });
}




