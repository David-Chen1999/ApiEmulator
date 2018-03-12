<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-project" class="skip" tabindex="-1" style=""><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="create-project" class="content scaffold-create" style="" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.project}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.project}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form action="save" target="_top">
                <fieldset class="form">
                    <f:field property="title" bean="project" label="中文名称"></f:field>
                    <f:field property="name" bean="project" label="英文名称(没空格）"></f:field>
                    <f:field property="sourceDir" bean="project" label="目标目录"></f:field>
                    <f:field property="packageName" bean="project" label="JAVA包名"></f:field>
                    <f:field property="urlBase" bean="project" label="URL根目录，如https://2wintech.cn/ciji"></f:field>
                    <f:field property="description" bean="project" label="描述"></f:field>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
