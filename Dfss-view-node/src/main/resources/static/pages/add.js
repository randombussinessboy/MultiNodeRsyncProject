$(function () {
    initSelect()
})
var gSelect=""
function initSelect() {
$.ajax({
        url: URL_TASK_GET_NODES,
        type: 'get',
        dataType:"json",
        success: function (data) {
           if(data && data.rows){
               gSelect = data.rows;
               // var html="<option value=''>请选择源服务器</option>"
               var html="";
                for(var i = 0;i<data.rows.length;i++){
                    var temp = data.rows[i];
                    html+="<option value='"+temp.url+"'>"+temp.serviceName+"</option>"

                }
                $("#taskSel").html(html)
               taskSelChange();
           }
        }
    });
}
let gUrl=""
let gName=""
function taskSelChange() {
    gUrl = $("#taskSel").val();
    gName = $("#taskSel").find("option:selected").text();
    let setting = {
        check: {
            enable: true,
            chkStyle : "radio",
            autoCheckTrigger: false,
            radioType:"all",
            chkboxType: {"Y": "", "N": ""}
        },
        async: {
            enable: true,
            url:URL_TASK_FOLDER,
            type:"get",
            autoParam:["fileUrl"],
            otherParam:{"sourceUrl":gUrl}
            // dataFilter: ajaxDataFilter
        },
        callback: {
            onCheck: zTreeOnCheck
        }
    };
    $.fn.zTree.init($("#treeDemo"), setting);

    $("[name=targetDivName]").remove();
    tarArr=[];
}
function ajaxDataFilter(treeId, parentNode, responseData) {
    if (responseData) {
        for(var i =0; i < responseData.length; i++) {
            if(!responseData[i].isParent){
                responseData[i].nocheck=true
            }
        }
    }
    return responseData;
};

function zTreeOnCheck(event, treeId, treeNode) {
    if(treeNode.isParent){
        $("#taskSel2").val("1")
    }else{
        $("#taskSel2").val("0")
    }
}


function taskSelChange2() {
    let type = $("#taskSel3").val();
    if(type==1){
        $("#timeFormDiv").show();
    }else{
        $("#timeFormDiv").hide();
    }
}

let existArr=[];

function getTitle() {
    let title = ""
    for(let i = 0;i<gSelect.length;i++){
        if(gUrl!=gSelect[i].url){
            let flag = false;
            title = gSelect[i].serviceName;
            for(let j=0;j>existArr.length;j++){
                if(title==existArr[j]) {
                    title = "";
                    break;
                }
            }
            break;
        }
    }
    existArr.push(title);
    return title;
}

let x=0;

function addTarget() {
    let total = $("[name=targetDivName]")
    if(total.length == gSelect.length-1 ) {
        alert("目标节点已达最大数量，不可创建新的")
        return ;
    }
    x++;
    let html = "";
    html+="<div name='targetDivName' id=\"targetDiv"+x+"\"class=\"panel panel-info\">";
    html+="<div class=\"panel-heading\">目标服务器&nbsp;&nbsp;" +
        "<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick='targetDel("+x+")' >"+
        "<span class=\"glyphicon glyphicon-remove\" ></span></button></div>";
    html+="<div class=\"panel-body\">";
    html+="<div class=\"row\">";
    html+=" <div class=\"col-sm-3\">目标服务器</div>" ;
    html+=" <div class=\"col-sm-4\">" +
        " <select id=\"targetSel"+x+"\" class=\"form-control\" onchange=\"targetChange(this,"+x+")\">"+
        targetOptions()+"</select></div>"
    html+="</div>";
    html+="<div class=\"row\">";
    html+="<div class=\"col-sm-3\">文件夹</div>" ;
    html+="<div class=\"col-sm-6\"> <ul id=\"targetTree"+x+"\" class=\"ztree\"></ul></div>"
    html+="</div>";
    html+="</div>";
    html+="</div>";
    $("#targetDiv").append(html);
    let setting = targetTreeSet(null,$("#targetSel"+x).val())
    $.fn.zTree.init($("#targetTree"+x), setting);
}

//目标节点的删除
function targetDel(num) {
    let targetUrl = $("#targetSel"+num).val();
    for (let i = 0; i < tarArr.length; i++) {
        if(tarArr[i].targetUrl ==targetUrl){
            tarArr.splice(i, 1);
        }
    }
    $("#targetDiv"+num).remove();
}
//目标节点的下拉框
function targetOptions() {
    var html="";
    for(let i = 0;i<gSelect.length;i++){
        if(gUrl!=gSelect[i].url){
            html+="<option value='"+gSelect[i].url+"'>"+gSelect[i].serviceName+"</option>"
        }
    }
    return html;
}

//目标节点的树形配置
function targetTreeSet(source,url) {

    let sUrl = "";
    if(source)  {
        sUrl = $(source).val();
    }else if(url){
        sUrl = url;
    }

    let setting = {

        check: {
            enable: true,
            chkStyle : "radio",
            autoCheckTrigger: false,
            radioType:"all",
            chkboxType: {"Y": "", "N": ""}
        },
        async: {
            enable: true,
            url:URL_TASK_FOLDER,
            type:"get",
            autoParam:["fileUrl"],
            otherParam:{"sourceUrl":sUrl}
        },
        callback: {
            onCheck: zTreeOnCheck2
        }
    };
    return setting;
}

let tarArr=[]
function zTreeOnCheck2(event, treeId, treeNode) {
    let tempx = treeId.substring(treeId.length-1,treeId.length)
    let targetUrl = $("#targetSel"+tempx).val();
    let targetFile = treeNode.fileUrl;
    let ojb={
        targetUrl:targetUrl,
        targetFile:targetFile
    }
    if(tarArr.length==0){
        tarArr.push(ojb);
    }else {
        let inarr =true;
        for (let i = 0; i < tarArr.length; i++) {
            if(tarArr[i].targetUrl ==ojb.targetUrl){
                tarArr[i] = ojb;
                inarr = false;
            }
        }
        if(inarr) tarArr.push(ojb);
    }
}
function targetChange(source,num) {
        // alert($(source).val())
    let setting = targetTreeSet(source)

    $.fn.zTree.init($("#targetTree"+num), setting);

}


function addTask() {
    let isDirectonry ,isTimmingTask , minutes;
    let treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    let nodes = treeObj.getCheckedNodes();
    if(nodes && nodes.length>0){
        let fileUrl = nodes[0].fileUrl;
        // console.log( fileUrl +"--"+ gUrl);
        let dire = $("#taskSel2").val();
        if(dire == 1){
            isDirectonry = true;
        }else{
            isDirectonry = false;
        }
        let ti = $("#taskSel3").val();
        if(ti == 1){
            isTimmingTask = true;
        }else{
            isTimmingTask = false;
        }
        minutes = $("#taskTime").val();
        if(isTimmingTask && (minutes==null || minutes == "")){
            alert("源服务器："+gName+"，请填写定时时间")
            return false
        }
        minutes = parseInt(minutes)
        if(isNaN(minutes))  minutes=0;
        var data = {
            fileUrl:fileUrl,
            sourceUrl:gUrl,
            directonry:isDirectonry,
            timmingTask:isTimmingTask,
            minutes:minutes
        }
        if(tarArr.length==0){
            alert("源服务器："+gName+"，请添加目的节点并选择目的节点文件夹")
            return false
        }else {
            data.targetList = tarArr
            // console.log( data );
            return data;
        }
    }else{
        alert("源服务器："+gName+"，请选择一个文件")
        return false
    }
}
