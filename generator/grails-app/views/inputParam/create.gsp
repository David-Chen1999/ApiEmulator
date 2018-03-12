<div id="create-inputParam" class="content scaffold-create" role="main">
    <g:hasErrors bean="${this.inputParam}">
    <ul class="errors" role="alert">
        <g:eachError bean="${this.inputParam}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
    </ul>
    </g:hasErrors>
    <form method="post" id="createInputForm" action="<g:createLink controller="inputParam" action="save" />" onsubmit="createInput();">
        <fieldset class="form">
            <f:field property="name" bean="inputParam"/>
            <f:field property="type" bean="inputParam"/>
            <f:field property="description" bean="inputParam"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
        </fieldset>
    </form>
</div>
<script>
    function createInput() {
        event.preventDefault();
        var options = {
            target: "#create-inputParam"
        };

        $("#createInputForm").ajaxSubmit(options);
    }

</script>