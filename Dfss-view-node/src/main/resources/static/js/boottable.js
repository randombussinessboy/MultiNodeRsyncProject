$(function () {
    tableInit()
})

function tableInit() {
    $('#table').bootstrapTable({
        url: '/list.do', // 请求后台的URL（*）
        method: 'get', // 请求方式（*）
        contentType: "application/x-www-form-urlencoded",//post 必须制定类型，否则不能正常传值
        // toolbar: '#toolbar', // 工具按钮用哪个容器
        striped: true, // 是否显示行间隔色
        cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, // 是否显示分页（*）
        sortName: "id",//默认排序列
        sortable: true, // 是否启用排序
        sortOrder: "asc", // 排序方式
        queryParams: tableQueryParams,// 传递参数（*）
        sidePagination: "server", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pageSize: 3, // 每页的记录行数（*）
        pageList: [10, 15, 20], // 可供选择的每页的行数（*）
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
            field: 'account_',
            title: '账号'
        },{
            field: 'name_',
            title: '姓名',
        },{
            field: 'birthday_',
            title: '出生年月',
        }, {
            field: 'id_',
            title: '操作',
            formatter: function (data, row, index) {
                var temp = ""
                temp = "<a href='javascript:void(0)' onclick=edit('" + data + "','" + row.name + "','" + row.account + "','" + row.birthday +"')>编辑</a>  &nbsp;&nbsp;";
                temp += "<a href='javascript:void(0)' onclick=dele('" + data + "')>删除</a>   &nbsp;&nbsp;";
                temp += "<a href='javascript:void(0)' onclick=roleJoin('" + data + "')>关联角色</a>  ";

                return temp;
            }
        }]
    });
}

function tableQueryParams(params) {
        var page = (params.offset / params.limit) + 1;
        var temp = {
            size: params.limit, // 页面大小
            page: page, // 第几页
            name: $("#search_name").val(),
            order: params.order,
            sort: params.sort
        };
        return temp;
}
