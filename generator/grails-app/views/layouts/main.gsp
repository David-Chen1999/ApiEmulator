<!doctype html>
<html lang="en" class="no-js" style="height: 100%">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <g:layoutHead/>
</head>
<body style="height: 100%">

    <div class="navbar navbar-default navbar-static-top" role="navigation" >
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/#">
                    <i class="fa grails-icon">
                        <asset:image src="logo.png" width="30px"/>
                    </i> 奇松App生成工具
                </a>
            </div>
            <div class="navbar-collapse collapse" aria-expanded="false" style="height: 0.8px;">
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">当前项目:${project?.name} <span class="caret"></span></a>


                        <ul class="dropdown-menu">
                            <g:each in="${list}" var="project">
                                <li style="width: 100%">
                                    <g:link action="show" resource="${project}">${project?.name}</g:link>
                                </li>
                            </g:each>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <g:link action="create" >新建</g:link>
                    </li>
                    <g:if test="${project?.id}">
                        <li class="dropdown">
                            <g:link action="edit" resource="${project}">编辑</g:link>
                        </li>
                    </g:if>
                </ul>
            </div>
        </div>
    </div>
    <g:layoutBody/>


    <div class="footer" role="contentinfo"></div>

    <div id="spinner" class="spinner" style="display:none;">
        <g:message code="spinner.alt" default="Loading&hellip;"/>
    </div>



</body>
</html>
