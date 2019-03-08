<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.10.0/fullcalendar.min.css" rel='stylesheet'/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar-scheduler/1.9.4/scheduler.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.10.0/fullcalendar.print.css" rel='stylesheet' media='print' >
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.10.0/fullcalendar.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar-scheduler/1.9.4/scheduler.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.10.0/locale-all.js"></script>
    <script>
        id = 0
        $(function() { // document ready

            $('#calendar').fullCalendar({
                locale:'zh-cn',
                selectable:true,
                select:function(startDate, end, allDay, jsEvent, view ){
                    document.getElementById("alert").style.display="block";
                    document.getElementById("startDate").value=$.fullCalendar.formatDate(startDate,"YYYY-MM-DD HH:mm:ss");
                    document.getElementById("endDate").value=$.fullCalendar.formatDate(end,"YYYY-MM-DD HH:mm:ss");;
                    $("#btn_div").show()
                }, 
                height : "auto",  
                header: {
                    left: 'today prev,next',
                    center: 'title,prevYear,nextYear',
                    right: 'timelineDay,agendaWeek,month,listDay,listWeek'
                },
                // timeFormat: 'H:mm',
                defaultView: 'month',
                events: {
                    url: 'events',
                    type: 'POST',
                    data:{m:"list"},
                    cache: true,
                    error: function() {
                        alert('there was an error while fetching events!');
                    }
                },eventClick: function(calEvent, jsEvent, view) {
                    console.log(jsEvent)
                    $.post(
                        {
                            url:"events",
                            data:{id:calEvent.id,m:"byId"},
                            type:"post",
                            dataType:"json",
                            success:function(data){
                                $("#startDate").val(data.start).show();
                                $("#endDate").val(data.end).show();
                                $("#remark").val(data.remark);
                                $("#title").val(data.title);
                                id = data.id;
                                $("#alert").show();
                                if (data.color=='#339966'){
                                    $("#btn_div").hide()
                                    return
                                }
                                $("#btn_div").show()
                                $("#btn_div").prepend("<input type='button' id='don' value='完成' style='width: 3em;'onclick='donf("+id+")'>")
                                    .prepend("<input type='button' id='del' value='删除' style='width: 3em;'onclick='delf("+id+")'>").css("min-width","12em");
                            },
                            error:function(err){
                            }
                        }
                    );
                },
                  eventRender: function(event, element,view) {
                    $(element).find('.fc-content').append("<div class='content'>"+event.remark+"</div>")
                    $(element).find(".fc-time").remove();
                  }
            });
        });

    </script>
    <style>

        body {
            margin: 0;
            padding: 0;
            font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif,"Songti SC","Songti TC";
            font-size: 14px;
        }
        .fc-license-message{
            display: none;
        }

        #calendar {
            max-width: 900px;
            margin: 50px auto;
        }
        .content{
            white-space:normal;
            font-weight: lighter;
        }
        .fc-unthemed td.fc-today {
            background: #ff74FF;
        }
    </style>
</head>
<body>
<div id='calendar'></div>
<div id="alert" style="background-color: aliceblue;height: 100%;width: 100%;z-index: 99999;position: fixed;top: 0px;display: none">
    <div style="top: 0.1%;right: 15%;position: fixed;font-size: large" class="close" onclick="xClose()">X</div>
    <div style="width: 50%;font-size: large;margin: auto;background-color:azure;font-size: xx-large">
        <div style="margin: auto;width: 2em;">标题</div>
    </div>
    <form style="margin: auto;width: 50%;margin-top: 10px;" id="form">
        标题：<input type="text" name="title" style="width: 100%;height: 5em;" id="title"><br/>
        <input type="text" id="startDate" name="startDate" style="display: none"><br/>
        <input type="text" id="endDate" name="end" style="display: none"><br/>
        备注：<textarea name="remark" style="width: 100%;height: 40%;" id="remark"></textarea>
        <div style="width: 20%;margin-right: auto;margin-left: auto;min-width: 6em;" id="btn_div">
            <input type="button" value="提交" style="width: 3em;" onclick="subm()">
            <input type="button" value="重置" style="width: 3em;" onclick="clearContent()">
        </div>
    </form>
</div>
<script type="application/javascript">
    function xClose() {
        $("#del").remove();
        $("#don").remove();
        $("#alert").hide();
        document.getElementById("form").reset();
        $("#startDate").hide();
        $("#endDate").hide();
        id = 0;
        $("#btn_div").css("min-width","6em");

    }
    function clearContent() {
        $("#title").val("");
        $("#remark").val("");
    }
    function delf(id) {
        if(confirm("删除此事件?")){
            $.post({url:"events",
                data:{id:id,m:"del"},
                type:"post",
                dataType:"json",
                success:function(data){
                    $('#calendar').fullCalendar("refetchEvents")
                },error:function (data) {

                }
            })
            xClose()
        }
    }
    function donf(id) {
        if(confirm("完成此事件?")){
            $.post({url:"events",
                data:{id:id,m:"don"},
                type:"post",
                dataType:"json",
                success:function(data){
                    $('#calendar').fullCalendar("refetchEvents")
                },error:function (data) {

                }
            })
            xClose()
        }
    }
    function subm() {
        if (!$("#title").val()){
            alert("空数据")
            return
        }
        method = ''
        if (id){
            method = 'mod'
        } else {
            method = 'add'
        }
        $.post({
            url:"events",
            type:"post",
            data:{m:method,start:$("#startDate").val(),end:$("#endDate").val(),title:$("#title").val(),remark:$("#remark").val(),id:id},
            dataType:"json",
            success:function(data){
                $('#calendar').fullCalendar("refetchEvents")
                document.getElementById("alert").style.display="none";
                xClose()
            },
            error:function (err) {

            }
        });
    }
</script>
</body>
</html>
