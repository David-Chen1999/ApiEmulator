<div class="container-fluid">
    <div class="row" style="margin-top: 5px">
    </div>
    <nav class="navbar navbar-default" style="margin-top: 15px">
        <div class="container-fluid">
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                %{--<ul class="nav navbar-nav navbar-left">--}%
                    %{--<li class="dropdown">--}%
                        %{--<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">接口版本 <span class="caret"></span></a>--}%
                        %{--<ul class="dropdown-menu">--}%
                            %{--<g:each in="${apiVersions}" var="apiVersion">--}%
                                %{--<li><a href="#">${apiVersion}</a></li>--}%
                            %{--</g:each>--}%
                            %{--<li role="separator" class="divider"></li>--}%
                            %{--<li><a href="#" onclick="newVersion();">新增版本</a></li>--}%
                        %{--</ul>--}%
                    %{--</li>--}%
                %{--</ul>--}%
                <form class="navbar-form navbar-right">
                    %{--<div class="form-group">--}%
                        %{--<input type="text" class="form-control" placeholder="Search">--}%
                    %{--</div>--}%
                    <button type="submit" class="btn btn-default" onclick="ajaxGen();">生成代码</button>
                </form>
            </div>
        </div>
    </nav>

    <div style="width: 100%">
        <div class="col-md-3">
            <h3 style="width: 100%">
                接口列表
                <button type="button" class="btn btn-default pull-right" onclick="newInterface()">
                    <i class="glyphicon glyphicon-plus " aria-hidden="true"></i>
                </button>
            </h3>

            <g:ajaxPanel url="${createLink(controller: "serverInterface", action: "list")}" id="interfaceListAjaxPanel">
                <g:render template="/tmpls/interfacelist"/>
            </g:ajaxPanel>
        </div>

        <div class="col-md-3">

                <h3 style="width: 100%">
                    输入参数
                    <button type="button" class="btn btn-default pull-right" onclick="newInput()">
                        <i class="glyphicon glyphicon-plus " aria-hidden="true"></i>
                    </button>
                </h3>

            <g:ajaxPanel url="${createLink(controller: "inputParam", action: "listInputs")}" id="inputsListAjaxPanel">
                <g:render template="/tmpls/inputParams"/>
            </g:ajaxPanel>
        </div>

        <div class="col-md-3">

            <h3 style="width: 100%">
                输出结果
                <button type="button" class="btn btn-default pull-right" onclick="newOutput()">
                    <i class="glyphicon glyphicon-plus " aria-hidden="true"></i>
                </button>
            </h3>

            <g:ajaxPanel url="${createLink(controller: "outputParam", action: "listOutputs")}" id="outputListAjaxPanel">
                <g:render template="/tmpls/outputParams"/>
            </g:ajaxPanel>

        </div>

        <div class="col-md-3">
            <h3   style="width: 100%">
                测试用例
                <span   data-toggle="tooltip"
                        title="<img width='500px' src='${createLink(absolute:"/")}assets/testcase_help.png'/>"
                        data-html="true"
                        data-placement="auto"
                        data-container="body"
                        data-delay="500"
                        >?</span>
                <button type="button" class="btn btn-default pull-right" onclick="newTestcase()">
                    <i class="glyphicon glyphicon-plus " aria-hidden="true"></i>
                </button>
            </h3>
            <g:ajaxPanel url="${createLink(controller: "testcase", action: "listTestcases")}" id="testcaseListAjaxPanel">
                <g:render template="/tmpls/testcases"/>
            </g:ajaxPanel>

        </div>
    </div>
</div>

%{--以下是几个输入对话框--}%
<div class="modal fade" id="newInterfaceDialog" tabindex="-1" role="dialog" aria-labelledby="newInterfaceDialog" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="buttion" id="newInterfaceDialogClose" class="close" data-dismiss="modal" aria-label="Close" onclick="refreshAjaxPanel('interfaceListAjaxPanel')"><span aria-hidden="true">&times;</span> </button>
                <h4 class="modal-title" >新增接口</h4>
            </div>
            <div class="modal-body" style="height: 100%;width: 98%">
                <g:ajaxPanel url="${createLink(controller:"serverInterface", action:"create")}" id="newInterfaceDialogPanel"/>
            </div>
        </div>

    </div>
</div>

<div class="modal fade" id="newInputDialog" tabindex="-1" role="dialog" aria-labelledby="newInputDialog" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="buttion" class="close" data-dismiss="modal" aria-label="Close" onclick="refreshAjaxPanel('inputsListAjaxPanel')"><span aria-hidden="true">&times;</span> </button>
                <h4 class="modal-title" >新增输入参数</h4>
            </div>
            <div class="modal-body" style="height: 100%;width: 98%">
                <g:ajaxPanel url="${createLink(controller:"inputParam", action:"create")}" id="newInputDialogPanel"/>
            </div>
        </div>

    </div>
</div>

<div class="modal fade" id="newOuputDialog" tabindex="-1" role="dialog" aria-labelledby="newOuputDialog" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="buttion" class="close" data-dismiss="modal" aria-label="Close" onclick="refreshAjaxPanel('outputListAjaxPanel')"><span aria-hidden="true">&times;</span> </button>
                <h4 class="modal-title" >新增输出参数</h4>
            </div>
            <div class="modal-body" style="height: 100%;width: 98%">
                <g:ajaxPanel url="${createLink(controller:"outputParam", action:"create")}" id="newOutputDialogPanel"/>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editOuputDialog" tabindex="-1" role="dialog" aria-labelledby="editOuputDialog" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="buttion" class="close" data-dismiss="modal" aria-label="Close" onclick="refreshAjaxPanel('outputListAjaxPanel')"><span aria-hidden="true">&times;</span> </button>
                <h4 class="modal-title" >编辑输出参数</h4>
            </div>
            <div class="modal-body" style="height: 100%;width: 98%">
                <g:ajaxPanel url="${createLink(controller:"outputParam", action:"edit")}" id="editOutputDialogPanel"/>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="newTestcaseDialog" tabindex="-1" role="dialog" aria-labelledby="newTestcaseDialog" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="buttion" class="close" data-dismiss="modal" aria-label="Close" onclick="refreshAjaxPanel('testcaseListAjaxPanel')"><span aria-hidden="true">&times;</span> </button>
                <h4 class="modal-title" >创建测试用例</h4>
            </div>
            <div class="modal-body" style="height: 100%;width: 98%">
                <g:ajaxPanel url="${createLink(controller:"testcase", action:"create")}" id="newTestcaseDialogPanel"/>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editTestcaseDialog" tabindex="-1" role="dialog" aria-labelledby="editTestcaseDialog" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="buttion" class="close" data-dismiss="modal" aria-label="Close" onclick="refreshAjaxPanel('testcaseListAjaxPanel')"><span aria-hidden="true">&times;</span> </button>
                <h4 class="modal-title" >编辑测试用例</h4>
            </div>
            <div class="modal-body" style="height: 100%;width: 98%">
                <g:ajaxPanel url="${createLink(controller:"testcase", action:"show")}" id="editTestcaseDialogPanel"/>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editInputDialog" tabindex="-1" role="dialog" aria-labelledby="editInputDialog" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="buttion" class="close" data-dismiss="modal" aria-label="Close" onclick="refreshAjaxPanel('inputsListAjaxPanel')"><span aria-hidden="true">&times;</span> </button>
                <h4 class="modal-title" >编辑输入参数</h4>
            </div>
            <div class="modal-body" style="height: 100%;width: 98%">
                <g:ajaxPanel url="${createLink(controller:"inputParam",action:"edit")}" id="editInputDialogPanel"/>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="progressIndicatorDialog" tabindex="-1" role="dialog" aria-labelledby="progressIndicatorDialog" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="buttion" class="close" data-dismiss="modal" aria-label="Close" onclick=""><span aria-hidden="true">&times;</span> </button>
                <h4 class="modal-title" >生成进度</h4>
            </div>
            <div class="progress">
                <div id="genProgressBar" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">
                    100%
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    function newInterface(){
        refreshAjaxPanel("newInterfaceDialogPanel");
        $("#newInterfaceDialog").modal("show");
    }

    function newInput(){
        refreshAjaxPanel("newInputDialogPanel");
        $("#newInputDialog").modal("show");
    }

    function newOutput(){
        refreshAjaxPanel("newOutputDialogPanel");
        $("#newOuputDialog").modal("show");
    }
    function newTestcase(){
        refreshAjaxPanel("newTestcaseDialogPanel");
        $("#newTestcaseDialog").modal("show");
    }

    function editOutputParam(id){
        var url = $("#editOutputDialogPanel").attr("url");
        var newUrl = url + "/" + id;
        $("#editOutputDialogPanel").attr("url",newUrl);
        refreshAjaxPanel("editOutputDialogPanel");
        $("#editOutputDialogPanel").attr("url",url);
        $("#editOuputDialog").modal("show");
    }

    function editTestcase(id){
        var url = $("#editTestcaseDialogPanel").attr("url");
        var newUrl = url + "/" + id;
        $("#editTestcaseDialogPanel").attr("url",newUrl);
        refreshAjaxPanel("editTestcaseDialogPanel");
        $("#editTestcaseDialogPanel").attr("url",url);
        $("#editTestcaseDialog").modal("show");
    }



    function editInputParam(id){
        var url = $("#editInputDialogPanel").attr("url");
        var newUrl = url + "/" + id;
        $("#editInputDialogPanel").attr("url",newUrl);
        refreshAjaxPanel("editInputDialogPanel");
        $("#editInputDialogPanel").attr("url",url);
        $("#editInputDialog").modal("show");
    }

    var selectedInterface = undefined;
    function selectRow(trId,id) {
        if(id){
            $.post("${createLink(controller:"serverInterface",action:"ajaxSelect")}",{"id":id},function (data,status) {
                if("200" == data.status){
                    markSelection(trId);
                    refreshAjaxPanel("inputsListAjaxPanel");
                    refreshAjaxPanel("outputListAjaxPanel");
                    refreshAjaxPanel("testcaseListAjaxPanel");
                }else{
                    alert("服务器错误！")
                }
            })
        }else {
            markSelection(trId);
        }
    }

    function markSelection(trId) {
        if(selectedInterface){
            selectedInterface.removeClass('activeInterface');
        }
        selectedInterface = $('#'+trId);
        selectedInterface.addClass('activeInterface');
    }

    var progressTimer = undefined;
    function updateProgress(result) {
        debugger
        var percent = result.percent;
        var msg = result.msg;

        $("#genProgressBar").width("25%");
        $("#genProgressBar").html(msg);
        if(percent == 100){
            progressTimer.cancel();
        }
    }
    function ajaxGen() {
        var result = confirm("生成代码需要等待一定的时间，你确认现在生成吗？");
        if(!result){
            return;
        }

        $("#progressIndicatorDialog").modal("show");

        $.post("${createLink(controller:"project",action:"ajaxGen")}",null,function (data,status) {
            if("200" == data.status){
                progressTimer = setInterval(function () {
                    $.get("${createLink(controller:"project", action:"genProgress")}",updateProgress);
                },500);
            }
        })
    }

    function delInterface(id) {
        var result = confirm("你确认要删除吗？");
        if(!result){
            return;
        }
        $.post("${createLink(controller:"serverInterface",action:"ajaxDel")}",{"id":id},function (data,status) {
            if("200" == data.status){
                alert("删除成功！")
                refreshAjaxPanel('interfaceListAjaxPanel');
                refreshAjaxPanel("inputsListAjaxPanel");
                refreshAjaxPanel("outputListAjaxPanel");
                refreshAjaxPanel("testcaseListAjaxPanel");
            }else{
                alert("删除失败！")
            }
        })
    }

    function delInputParam(id) {
        var result = confirm("你确认要删除吗？");
        if(!result){
            return;
        }
        $.post("${createLink(controller:"inputParam",action:"ajaxDel")}",{"id":id},function (data,status) {
            if("200" == data.status){
                refreshAjaxPanel('inputsListAjaxPanel');
            }else{
                alert("删除失败！")
            }
        })
    }

    function delOutputParam(id) {
        var result = confirm("你确认要删除吗？");
        if(!result){
            return;
        }
        $.post("${createLink(controller:"outputParam",action:"ajaxDel")}" ,{"id":id},function (data,status) {
            if(data.exception){
                alert("删除失败！" + data.exception)
            }else{

                refreshAjaxPanel('outputListAjaxPanel');
            }
        })
    }

    function delTestcase(id) {
        var result = confirm("你确认要删除吗？");
        if(!result){
            return;
        }

        $.post("${createLink(controller:"testcase",action:"ajaxDel")}",{"id":id},function (data,status) {
            if(data.exception){
                alert("删除失败！" + data.exception)
            }else{

                refreshAjaxPanel('testcaseListAjaxPanel');
            }
        })
    }
    selectRow('interface0');

    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
</script>