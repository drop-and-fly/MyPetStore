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
              @click="logs=[]">���</span>
        <p @click="click_log_status" style="padding: 2px;">
            {{log_status?"�رյ�����Ϣ":"�򿪵�����Ϣ"}}</p>
        <span v-if="log_status" v-for="(item,index) in logs">
					<p>{{index+1}}.{{item}}</p>
        </span>
    </div>
</div>

<%!
    //�������ַ���
    String trasverseLogStr;
    int logNums;
    String[] logArray;
    int cnt = 0;

    public void updateLogArray()
    {
        //��JS��������Ե���JSP����
        LogDAO.getAllLogEntry();
        LogDAO.trasverseInt();
        logNums = LogDAO.getLogList().size();
        logArray = (String[]) LogDAO.getLogList().toArray(new String[logNums]);
        cnt = 0;
    }

	//JSP����ֻ��ִ��һ�Ρ�
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
            //�����ݿ������־��Ϣ��servlet
            updateLogArray: function () {
                <%updateLogArray();//JS����JSP�ɹ�%>
            },
            //��servlet��ȡ��Ϣ��ÿ�ζ�ȡһ����־
            trasverLogStr: function () {
                "<%traverseLog();%>"
                var str = "<%=trasverseLogStr%>";//ע�⣺��JSP��ֵ��JS��һ��Ҫ��˫���ţ�����������ᱨ��
                return str;
            },
            getArrayFromJSP: function () {
                var logNums = parseInt("<%=logNums%>");

                //JSP����ֱ�Ӵ������JS��ֻ��ͨ�������﷨��
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

                this.logs.push('����' + logNums + '����־');
                for (var i = 0; i != logNums; i++) {
                    //this.logs.push(this.trasverLogStr());
                    this.logs.push(this.getArrayFromJSP()[i])
                }
            },

            // url��������
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