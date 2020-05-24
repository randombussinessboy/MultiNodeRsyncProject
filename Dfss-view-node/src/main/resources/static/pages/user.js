$(function () {

    $("#userDiv").show();
    tableInit();
    $('#modal').on('hidden.bs.modal', function () {
        $('#form')[0].reset();
    });
})


function tableInit() {
    $('#tableOne').bootstrapTable({
        url: '/getUser', // 请求后台的URL（*）
        method: 'get', // 请求方式（*）
        contentType: "application/x-www-form-urlencoded",//post 必须制定类型，否则不能正常传值
        toolbar: '#toolOne', // 工具按钮用哪个容器
        striped: true, // 是否显示行间隔色
        cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, // 是否显示分页（*）
        queryParams: tableQueryParams,// 传递参数（*）
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
            field: 'name',
            title: '用户名称'
        },{
            field: 'pass',
            title: '密码',
        }
        ,{
            field: 'name',
            title: '操作',
            formatter: function (data, row, index) {
                var temp = ""
                temp += "<button type=\"button\" class=\"btn btn-warning btn-xs\" onclick=\"dele('" + data + "')\">删除</button>&nbsp;";
                return temp;
            }
        }
        ]
    });
}

function tableQueryParams(params) {
    var page = (params.offset / params.limit) + 1;
    var temp = {
        size: params.limit, // 页面大小
        page: page, // 第几页
        key: $("#inputKey").val(),
        order: params.order,
        sort: params.sort
    };
    return temp;
}
var url="/add";
function showModal(type) {
    $('#modal').modal('show');
    if(type==1){
        url="/login";
        $("#btn_save").text("登录")
    }else if(type == 2){
        url ="/add";
        $("#btn_save").text("新增")
    }
}

function btnClick(type) {

    $.ajax({
        url: url,
        type: 'post',
        dataType:"json",
        data:{
            name:$("#inputName").val(),
            pass:$("#inputPass").val()

        },
        success: function (data) {
           if(data && data.code==0){
               alert(data.msg)
           }else if(data && data.code ==1){
               $('#modal').modal('hide');
               $("#tableOne").bootstrapTable('destroy');
               tableInit();
           }
        }
        });
}

function dele(name) {
$.ajax({
        url: '/delete',
        type: 'post',
        dataType:"json",
        data:{
            name:name
        },
        success: function (data) {
        	
        	  if(data && data.code==0){
                  alert(data.msg)
              }else if(data && data.code ==1){
                  $('#modal').modal('hide');
                  $("#tableOne").bootstrapTable('destroy');
                  tableInit();
              }
        	
        }
    });
}