let i=0;
function addTab() {
    i++;
    addTabs({id:  i ,title:"任务"+i ,close: true,url:"/pages/add.html" });
}


function addTask() {
    var iframe = $("iframe");
    if(!iframe || iframe.length==0){
        alert("请先创建任务");
        return;
    }
    let arr=[];
    let flag=true;
    for(let i=0;i<iframe.length;i++){
       let result =  iframe[i].contentWindow.addTask();
       if(result === false){
           flag = false;
           break;
       }else{
           arr.push( iframe[i].contentWindow.addTask());
       }
    }
    if(!flag) return;
    // console.log( arr );

    $.ajax({
            url: URL_TASK_SUB,
            type: 'post',
            contentType: "application/json",
            data:JSON.stringify(arr),
            dataType:"json",
            success: function (data) {
                if(data.result=="success"){
                	$(".nav").empty();
                	$(".tab-content").empty();
                    alert("添加任务成功")
                }
            }
        });
}