<div id="create-serverInterface" class="content scaffold-create" role="main">
    <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${this.serverInterface}">
    <ul class="errors" role="alert">
        <g:eachError bean="${this.serverInterface}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
    </ul>
    </g:hasErrors>
    <form method="post" id="createInterfaceForm" action="<g:createLink controller="serverInterface" action="save"/>" onsubmit="createInterface();">
        <fieldset class="form">
            <f:field property="name" bean="serverInterface"/>
            <f:field property="description" bean="serverInterface"/>
            %{--<f:field property="relativeUrl" bean="serverInterface"/>--}%
            <f:field property="method" bean="serverInterface"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
        </fieldset>
    </form>
</div>
<script>
function createInterface() {
    event.preventDefault();
    var options = {
        target: '#create-serverInterface'   // target element(s) to be updated with server response
    };

    $("#createInterfaceForm").ajaxSubmit(options);
}

</script>