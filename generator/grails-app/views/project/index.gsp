<!doctype html>
<html style="height: 100%">
<head>
    <meta name="layout" content="main"/>
    <title>欢迎使用奇松App生成系统</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body style="height: 100%">
<div class="container-fluid">
            <div class="tabbable" id="tabs-460373">
                <ul class="nav nav-tabs pull-right" style="width: 100%;margin-top: 10px">
                    <li class="active">
                        <a href="#panel-server-interface" data-toggle="tab">接口定义</a>
                    </li>
                    %{--<li>--}%
                        %{--<a href="#panel-database" data-toggle="tab">数据库</a>--}%
                    %{--</li>--}%
                    <li >
                        <a href="#panel-app-page" data-toggle="tab">App页面</a>
                    </li>

                    <li >
                        <a href="#panel-third-party" data-toggle="tab">第三方类库</a>
                    </li>
                </ul>
                <div class="tab-content" style="width: 100%;margin-top: 10px">
                    <div class="tab-pane active" id="panel-server-interface">
                        <p>
                           <g:render template="/tmpls/interface"></g:render>
                        </p>
                    </div>
                    %{--<div class="tab-pane" id="panel-database">--}%
                        %{--<p>--}%
                            %{--I'm in Section 1.--}%
                        %{--</p>--}%
                    %{--</div>--}%
                    <div class="tab-pane" id="panel-app-page">
                            %{--<div  style="background-color: #f8f8f8;min-height: 600px;width:20%;float:left;">--}%
                                %{--<div class="row" style="vertical-align: middle">--}%
                                    <h3 style="left: 10px;display: inline-block">
                                        目标是制作一个跨平台的App UI 设计和代码生成工具，目前还在开发中！
                                    </h3>
                                    %{--<i class="glyphicon-plus pull-right" data-toggle="modal" data-target="#newAppPageDialog"--}%
                                       %{--style="right: 10px; width: 30px;height: 100%;background-color: #5bc0de;text-align: center;margin-top: 10px"></i>--}%
                                %{--</div>--}%
                                %{--<g:ajaxPanel url="${createLink(controller:"project",action: "ajaxListAppPages",resource: project)}" id="projectListAjaxPanel"/>--}%
                            %{--</div>--}%
                    </div>

                    <div class="tab-pane" id="panel-third-party" >
                        <h3 style="left: 10px;display: inline-block">
                            为App集成第三方类库定义和代码生成工具，目前还在开发中!
                        </h3>
                    </div>
                </div>
    </div>
    <div class="modal fade" id="newAppPageDialog" tabindex="-1" role="dialog" aria-labelledby="newAppPageDialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header" style="height: 100%; width: 95%">
                    <button type="button" id="newAppPageDialog_close" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">新页面</h4>
                </div>
                <div class="modal-body" style="height: 100%; width: 98%">
                    %{--<iframe width="100%" height="250px" style="margin-top: 10px" src="<g:createLink controller="appPage" action="create"/>"></iframe>--}%
                </div>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        $(function () {
            $("#newAppPageDialog_close").on("click",function () {
                refreshAjaxPanel("projectListAjaxPanel");
            });

            refreshAjaxPanel("projectListAjaxPanel");
        })


    </script>
</div></body>
</html>
