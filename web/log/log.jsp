<%@ page import="log.LogDAO" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>

<head>
    <%@ page contentType="text/html;charset=GBK" %>
    <meta charset="UTF-8">
    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/vue/2.2.2/vue.min.js"
            type="text/javascript" charset="utf-8"></script>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
    <style type="text/css">
        .logs {
            background: rgba(0, 0, 0, 0.5);
            color: white;
            position: fixed;
            top: 4em;
            right: 10em;
            width: 50em;
            border-radius: 5px;
            padding: 2px;
            overflow: auto;
            z-index: 999;

            overflow-y: scroll;
            padding-right: 10px;
            height: 500px;
        }
    </style>
</head>

<body>
<div class="app" id="app">
    <div class="logs" data-spy="scroll" data-target="#navbar-example"
         data-offset="0" v-bind:style="{ height: log_height}">
        <span style="position: absolute;right: 2em;top: 1.2em;"
              @click="logs=[]">清空</span>
        <p @click="click_log_status" style="padding: 2px;">
            {{log_status?"关闭调试信息":"打开调试信息"}}</p>
        <span v-if="log_status" v-for="(item,index) in logs">
					<p>{{index+1}}.{{item}}</p>
        </span>
    </div>
</div>

<%!
    //遍历用字符串
    String trasverseLogStr;
    int logNums;
    String[] logArray;
    int cnt = 0;

    public void updateLogArray()
    {
        //在JS函数里可以调用JSP方法
        LogDAO.getAllLogEntry();
        LogDAO.trasverseInt();
        logNums = LogDAO.getLogList().size();
        logArray = (String[]) LogDAO.getLogList().toArray(new String[logNums]);
        cnt = 0;
    }

	//JSP代码只会执行一次。
    public String traverseLog()
    {
        //        if (LogDAO.trasverseArrayList())
        //        {
        //            trasverseLogStr = LogDAO.trasverseStr;
        //        }
        //        else {trasverseLogStr = "null";}
        if (cnt < logNums)
        {return logArray[cnt];}
		return null;
    }
%>

<script>
    new Vue({
        el: '#app',
        data: {
            logs: [],
            log_height: '16em',
            log_status: true,
            userId: 'asd'
        },
        methods: {
            click_log_status: function () {
                if (this.log_status) {
                    this.log_status = false
                    this.log_height = '3em'
                } else {
                    this.log_status = true
                    this.log_height = '16em'
                }
            },
            //从数据库更新日志信息到servlet
            updateLogArray: function () {
                <%updateLogArray();//JS调用JSP成功%>
            },
            //从servlet读取信息，每次读取一条日志
            trasverLogStr: function () {
                "<%traverseLog();%>"
                var str = "<%=trasverseLogStr%>";//注意：从JSP传值给JS，一定要加双引号！而且这个不会报错
                return str;
            },
            getArrayFromJSP: function () {
                var logNums = parseInt("<%=logNums%>");

                //JSP不能直接传数组给JS，只能通过以下语法。
                var array = new Array();
                <% for(int i=0;i!=logNums;i++){%>
                array[<%=i%>]="<%=logArray[i]%>"
                <%}%>
                return array;
            },
            login: function () {
                this.updateLogArray();
                var userId = this.userId;

                var logNums = parseInt("<%=logNums%>");

                this.logs.push('共有' + logNums + '条日志');
                for (var i = 0; i != logNums; i++) {
                    //this.logs.push(this.trasverLogStr());
                    this.logs.push(this.getArrayFromJSP()[i])
                }
            },

            // url参数解析
            getUrlkey: function (url) {
                var params = {};
                if (url.indexOf("?") != -1) {
                    var urls = url.split("?");
                    var arr = urls[1].split("&");
                    for (var i = 0, l = arr.length; i < l; i++) {
                        var a = arr[i].split("=");
                        params[a[0]] = a[1];
                    }
                } else {
                }
                return params;
            }
        },

        mounted: function () {
            if (this.getUrlkey(window.location.href).userId) {
                this.userId = this.getUrlkey(window.location.href).userId
            }
            this.login()
        }
    })
</script>

</body>

</html>