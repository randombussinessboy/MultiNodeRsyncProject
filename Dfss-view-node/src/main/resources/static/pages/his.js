$(function () {
    tableInit();
    $('#myModalLabel').html("任务详情");
    $('#myModal').on('hidden.bs.modal', modalHiddenEvent);//模态框隐藏触发
})
function modalHiddenEvent() {
    $('#userForm')[0].reset();
    // $('#datetimepicker').datetimepicker('update',"");
}
let gdata;
//正在运行的节点
function tableInit() {
    $('#tableOne').bootstrapTable({
        url: URL_TASK_HIS, // 请求后台的URL（*）
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
        columns: [{
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
        }, {
            field: 'taskId',
            title: '操作',
            width:'140px',
            formatter: function (data, row, index) {
                var temp = ""
                temp += "<button type=\"button\" class=\"btn btn-info btn-xs\" onclick=\"getInfo('" + data + "')\">详情</button>&nbsp;";
                return temp;
            }
        }],
        onLoadSuccess:function (data) {
            gdata = data;
        }

    });
}

let oldArr=[]
function getInfo(id) {
    $.ajax({
        url: URL_TASK_HIS_DETAIL,
        type: 'get',
        data:{taskId:id},
        dataType:"json",
        success: function (data) {
            oldArr=[];
            gUrl=data.sourceUrl;//用于目标节点下拉框，去除源节点
            $("#mSourceUrl").val(data.sourceUrl)
            $("#mSourceFile").val(data.fileUrl)
            if(data.directonry){
                $("#mDir").val("1");
            }else {
                $("#mDir").val("0");
            }
            if(data.timmingTask){
                $("#mDsDiv").show();
                $("#mDs").val("1");
                $("#mTime").val(data.minutes);

            }else {
                $("#mDsDiv").hide();
                $("#mDs").val("0");
                $("#mTime").val(0);
            }

            if(data.targetList){

                let temp="";
                for(let i =0;i<data.targetList.length;i++){
                    data.targetList[i].flag = false;
                    temp+="目的节点："+data.targetList[i].targetUrl+"<br>"
                    temp+="节点文件："+data.targetList[i].targetFile+"<br><br>"
                }
                oldArr = data.targetList;
                $("#mTarget").html(temp);
            }
            $("#taskId").val(id);
            $('#myModal').modal('show');


        }
    });

    taskMsg(id);
}

function taskMsg(id) {
    $.ajax({
        url: URL_TASK_DETAIL_MSG,
        type: 'get',
        data:{taskId:id},
        dataType:"json",
        success: function (data) {
            var html="";
            for(var i=0;i<data.rows.length;i++){
                var temp =  data.rows[i];
                html += temp+"\n\n";
            }
            $("#mMsg").val(html)
        }
    });
}