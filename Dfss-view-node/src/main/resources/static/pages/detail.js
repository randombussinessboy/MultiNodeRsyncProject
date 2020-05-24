var Pregreesstimer;

$(function() {
	tableInit();
	initSelect();
	$('#myModalLabel').html("任务详情");
	$('#myModal').on('hidden.bs.modal', modalHiddenEvent);// 模态框隐藏触发
})
function modalHiddenEvent() {
	$('#userForm')[0].reset();
	$("#mUpdateTargetDiv").empty();
	tarArr = []
	clearInterval(Pregreesstimer);    
	// $('#datetimepicker').datetimepicker('update',"");
}
let gdata;
// 正在运行的节点
function tableInit() {
	$('#tableOne')
			.bootstrapTable(
					{
						url : URL_SYS2, // 请求后台的URL（*）
						method : 'get', // 请求方式（*）
						contentType : "application/x-www-form-urlencoded",// post
																			// 必须制定类型，否则不能正常传值
						toolbar : '#toolOne', // 工具按钮用哪个容器
						striped : true, // 是否显示行间隔色
						cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
						pagination : false, // 是否显示分页（*）
						sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
						pageNumber : 1, // 初始化加载第一页，默认第一页
						pageSize : 5, // 每页的记录行数（*）
						pageList : [ 5, 10, 15 ], // 可供选择的每页的行数（*）
						search : false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
						strictSearch : true,
						showColumns : true, // 是否显示所有的列
						showRefresh : true, // 是否显示刷新按钮
						minimumCountColumns : 2, // 最少允许的列数
						clickToSelect : true, // 是否启用点击选中行
						singleSelect : true,// 开启单选，默认为多选
						uniqueId : "id", // 每一行的唯一标识，一般为主键列
						showToggle : false, // 是否显示详细视图和列表视图的切换按钮
						cardView : false, // 是否显示详细视图
						detailView : false, // 是否显示父子表
						columns : [
								{
									field : 'taskType',
									title : '任务类型',
								},
								{
									field : 'taskState',
									title : '任务状态',
									formatter : function(data, row, index) {
										if (data == "正在运行") {
											return "<span style='color: green' >"
													+ data + "</span>";
										} else if (data == "暂停中") {
											return "<span style='color: red' >"
													+ data + "</span>";
										} else {
											return data;
										}

									}
								},
								{
									field : 'source',
									title : '同步源节点'
								},
								{
									field : 'destinations',
									title : ' 同步目的节点',
									formatter : function(data, row, index) {
										var html = "";
										for (var i = 0; i < data.length; i++) {
											html += data[i] + "</br>"
										}
										return html;
									}
								},
								{
									field : 'taskId',
									title : '操作',
									width : '140px',
									formatter : function(data, row, index) {
										var temp = ""
										if (row.taskState == "正在运行") {
											temp += "<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"pauseTask('"
													+ data
													+ "')\">暂停</button>&nbsp;";
										} else {
											temp += "<button type=\"button\" class=\"btn btn-success btn-xs\" onclick=\"startTask('"
													+ data
													+ "')\">执行</button>&nbsp;";
										}
										temp += "<button type=\"button\" class=\"btn btn-warning btn-xs\" onclick=\"deleTask('"
												+ data
												+ "')\">删除</button>&nbsp;";
										temp += "<button type=\"button\" class=\"btn btn-info btn-xs\" onclick=\"getInfo('"
												+ data
												+ "')\">详情</button>&nbsp;";
										return temp;
									}
								} ],
						onLoadSuccess : function(data) {
							gdata = data;
						}

					});
}

function pauseTask(id) {
	$.ajax({
		url : URL_TASK_PAUSE,
		type : 'get',
		data : {
			taskId : id
		},
		dataType : "json",
		success : function(data) {
			if (data && data.result == "success") {
				alert("暂停任务成功")
			} else {
				alert("暂停任务失败")
			}
		}
	});
}
function startTask(id) {
	$.ajax({
		url : URL_TASK_START,
		type : 'get',
		data : {
			taskId : id
		},
		dataType : "json",
		success : function(data) {
			if (data && data.result == "success") {
				alert("执行任务成功")
			} else {
				alert("执行任务失败")
			}
		}
	});
}

function deleTask(id) {
	$.ajax({
		url : URL_TASK_DELE,
		type : 'get',
		data : {
			taskId : id
		},
		dataType : "json",
		success : function(data) {
			if (data && data.result == "success") {
				$("#tableOne").bootstrapTable('destroy');
				tableInit();
				alert("删除任务成功")
			} else {
				alert("删除任务失败")
			}

		}
	});
}

// 获取所有节点信息，修改详情的时候要用
var gSelect = ""
function initSelect() {
	$.ajax({
		url : URL_TASK_GET_NODES,
		type : 'get',
		dataType : "json",
		success : function(data) {
			if (data && data.rows) {
				gSelect = data.rows;
			}
		}
	});
}
let oldArr = []
function getInfo(id) {
	$
			.ajax({
				url : URL_TASK_DETAIL,
				type : 'get',
				data : {
					taskId : id
				},
				dataType : "json",
				success : function(data) {
					oldArr = [];
					gUrl = data.sourceUrl;// 用于目标节点下拉框，去除源节点
					$("#mSourceUrl").val(data.sourceUrl)
					$("#mSourceFile").val(data.fileUrl)
					if (data.directonry) {
						$("#mDir").val("1");
					} else {
						$("#mDir").val("0");
					}
					if (data.timmingTask) {
						$("#mDsDiv").show();
						$("#mDs").val("1");
						$("#mTime").val(data.minutes);

					} else {
						$("#mDsDiv").hide();
						$("#mDs").val("0");
						$("#mTime").val(0);
					}

					if (data.targetList) {

						let temp = "";
						for (let i = 0; i < data.targetList.length; i++) {
							data.targetList[i].flag = false;
							temp += "目的节点：" + data.targetList[i].targetUrl
									+ "<br>"
							temp += "节点文件：" + data.targetList[i].targetFile
									+ "<br>"
							temp += "操作：<a href='javascript:void(0)'  onclick=oldOpera("
									+ i + ",this" + ")>删除该节点</a><br><br>"
						}
						oldArr = data.targetList;
						$("#mTarget").html(temp);
					}
					$("#taskId").val(id);
					$('#myModal').modal('show');

				}
			});

	taskMsg(id);
	taskProgress(id)
}

function oldOpera(i, obj) {
	oldArr[i].flag = !oldArr[i].flag;
	if (oldArr[i].flag) {
		$(obj).text("取消删除");
	} else {
		$(obj).text("删除该节点");
	}
}

// 目的节点动态代码 begin

let x = 0;
let gUrl = "";
function addTarget() {
	let total = $("[name=targetDivName]")
	if (total.length == gSelect.length - 1) {
		alert("目标节点已达最大数量，不可创建新的")
		return;
	}
	x++;
	let html = "";
	html += "<div name='targetDivName' id=\"targetDiv" + x
			+ "\"class=\"panel panel-info\">";
	html += "<div class=\"panel-heading\">目标服务器&nbsp;&nbsp;"
			+ "<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick='targetDel("
			+ x
			+ ")' >"
			+ "<span class=\"glyphicon glyphicon-remove\" ></span></button></div>";
	html += "<div class=\"panel-body\">";
	html += "<div class=\"row\">";
	html += " <div class=\"col-sm-3\">目标服务器</div>";
	html += " <div class=\"col-sm-4\">" + " <select id=\"targetSel" + x
			+ "\" class=\"form-control\" onchange=\"targetChange(this," + x
			+ ")\">" + targetOptions() + "</select></div>"
	html += "</div>";
	html += "<div class=\"row\">";
	html += "<div class=\"col-sm-3\">文件夹</div>";
	html += "<div class=\"col-sm-6\"> <ul id=\"targetTree" + x
			+ "\" class=\"ztree\"></ul></div>"
	html += "</div>";
	html += "</div>";
	html += "</div>";
	$("#mUpdateTargetDiv").append(html);
	let setting = targetTreeSet(null, $("#targetSel" + x).val())
	$.fn.zTree.init($("#targetTree" + x), setting);
}

// 目标节点的删除
function targetDel(num) {
	let targetUrl = $("#targetSel" + num).val();
	for (let i = 0; i < tarArr.length; i++) {
		if (tarArr[i].targetUrl == targetUrl) {
			tarArr.splice(i, 1);
		}
	}
	$("#targetDiv" + num).remove();
}
// 目标节点的下拉框
function targetOptions() {
	var html = "";
	for (let i = 0; i < gSelect.length; i++) {
		if (gUrl != gSelect[i].url) {
			html += "<option value='" + gSelect[i].url + "'>"
					+ gSelect[i].serviceName + "</option>"
		}
	}
	return html;
}

// 目标节点的树形配置
function targetTreeSet(source, url) {

	let sUrl = "";
	if (source) {
		sUrl = $(source).val();
	} else if (url) {
		sUrl = url;
	}

	let setting = {

		check : {
			enable : true,
			chkStyle : "radio",
			autoCheckTrigger : false,
			radioType : "all",
			chkboxType : {
				"Y" : "",
				"N" : ""
			}
		},
		async : {
			enable : true,
			url : URL_TASK_FOLDER,
			type : "get",
			autoParam : [ "fileUrl" ],
			otherParam : {
				"sourceUrl" : sUrl
			}
		},
		callback : {
			onCheck : zTreeOnCheck2
		}
	};
	return setting;
}

let tarArr = []
function zTreeOnCheck2(event, treeId, treeNode) {
	let tempx = treeId.substring(treeId.length - 1, treeId.length)
	let targetUrl = $("#targetSel" + tempx).val();
	let targetFile = treeNode.fileUrl;
	let ojb = {
		targetUrl : targetUrl,
		targetFile : targetFile
	}
	if (tarArr.length == 0) {
		tarArr.push(ojb);
	} else {
		let inarr = true;
		for (let i = 0; i < tarArr.length; i++) {
			if (tarArr[i].targetUrl == ojb.targetUrl) {
				tarArr[i] = ojb;
				inarr = false;
			}
		}
		if (inarr)
			tarArr.push(ojb);
	}
}
function targetChange(source, num) {
	// alert($(source).val())
	let setting = targetTreeSet(source)

	$.fn.zTree.init($("#targetTree" + num), setting);

}

// end

function updateTask() {
	var ii = 1;
	let total = $("[name=targetDivName]")
	if (total && total.length != tarArr.length) {
		alert("请为所有的目的节点选择文件夹")
		return;
	}

	let time = $("#mTime").val();
	let minutes = parseInt(time)
	if (isNaN(minutes))
		minutes = 0;
	let taskId = $("#taskId").val();

	for (let i = 0; i < oldArr.length; i++) {
		if (!oldArr[i].flag) {
			tarArr.push(oldArr[i])
		}
	}

	let data = {
		taskId : taskId,
		minutes : minutes,
		targetList : tarArr
	}
	$.ajax({
		url : URL_TASK_MODIFY,
		type : 'post',
		contentType : "application/json",
		data : JSON.stringify(data),
		dataType : "json",
		success : function(data) {
			if (data.result == "success") {
				alert("修改任务成功")
			}
		}
	});
}

function taskMsg(id) {
	$.ajax({
		url : URL_TASK_DETAIL_MSG,
		type : 'get',
		data : {
			taskId : id
		},
		dataType : "json",
		success : function(data) {
			var html = "";
			for (var i = 0; i < data.rows.length; i++) {
				var temp = data.rows[i];
				html += temp + "\n\n";
			}
			$("#mMsg").val(html)
		}
	});

}

function taskProgress(id) {
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('mProgress'));
	// 用户等待
	myChart.showLoading({
		text : 'Loding...'
	});

	getData(myChart,id); // 先执行一次，以防等十秒后才出现图表
	Pregreesstimer = setInterval(function() {// 定时器
		getData(myChart,id);
	}, 2*1000);// 每隔5秒刷新一次

}

function getData(myChart,id) {
	// 发送Ajax请求
	$.ajax({
		url : URL_TASK_PROGRESS,
		dataType : "json",
		data : {
			taskId : id
		},
		type : 'get',
		success : function(json) {//
			console.log("获取图表数据");
			console.log(json);
			var datas = json.datas; // 拿到后台的数据
			var option = option = {
				
					 title: {
				          text: '任务单轮次',
				          subtext: '速度&进度参考值',
				          x: 'center'
				        },

				xAxis : {
					name: '进度',
					type : 'category',
					boundaryGap : false,
					data : [ '0', '10','20%','30%' , '40%' ,'50%', '60%',
							'70%', '80%','90%', '100%' ]
				},
				yAxis : {
					name: '速度M/S',
					type : 'value'
				},
				series : [ {
					data : datas,
					
			        itemStyle : { 
			           color:{
			                type: 'linear',
			                x: 0,
			                y: 0,
			                x2: 0,
			                y2: 1,
			                colorStops: [{
			                    offset: 0, color: '#06B025' // 0% 处的颜色
			                }, {
			                    offset: 1, color: '#06B025' // 100% 处的颜色
			                }],
			                global: false // 缺省为 false
			           }
			        },
			        lineStyle:{
			            color: {
			                type: 'linear',
			                x: 0,
			                y: 0,
			                x2: 1,
			                y2: 1,
			                colorStops: [{
			                    offset: 0, color: '#06B025' // 0% 处的颜色
			                },{
			                    offset: 0.5, color: '#06B025' // 0% 处的颜色
			                }, {
			                    offset: 1, color: '#06B025' // 100% 处的颜色
			                }],
			                global: false // 缺省为 false
			            }
			        },
					type : 'line',
					areaStyle : {}
				} ]
			};
			// 结束
			myChart.hideLoading();
			myChart.setOption(option);
		}
	});
}
