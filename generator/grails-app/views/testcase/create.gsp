<%@ page import="generator.BootStrap" %>
<div id="create-testCase" class="content scaffold-create" role="main">
    <h1>创建测试用例</h1>
    <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${this.testCase}">
    <ul class="errors" role="alert">
        <g:eachError bean="${this.testCase}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
    </ul>
    </g:hasErrors>
    <form method="post" id="createTestcaseForm" action="<g:createLink controller="testcase" action="save" />" onsubmit="createTestcase();">
        <fieldset class="form">
            <f:field property="name" bean="testCase"/>
            <f:field property="success" bean="testCase"/>
            <f:field property="responseCode" bean="testCase"/>
            <f:field property="responseMsg" bean="testCase"/>
            <f:field property="description" bean="testCase"/>
        </fieldset>
        <h2>输入值</h2>
        <table>
            <thead>
            <tr>
                <th>name</th><th>type</th><th>value</th></tr>
            </thead>
            <g:each in="${inputs}" var="input">
                <tr>
                    <td>${input.name}</td><td>${input.type}</td>
                    <td><input type="text" name="i_${input.name}_value"/></td>
                </tr>
            </g:each>
        </table>
        <h2>输出值</h2>
        <table>
            <thead>
            <tr>
                <th>name</th><th>type</th><th>value</th></tr>
            </thead>
            <g:each in="${outputs}" var="outputParamValue">
                <tr>
                    <td>
                        ${outputParamValue.name}
                    </td>
                    <td>
                         ${outputParamValue.type}
                    </td>
                    <td>
                        <g:if test="${outputParamValue.type.name == generator.BootStrap.OBJECT ||outputParamValue.type.name ==BootStrap.ARRAY}">
                            <g:each in="${outputParamValue.fields}" var="f">
                                <input type="text" placeholder="${f.name}_${f.type}" name="o_${outputParamValue.name}_${f.name}_value"/>
                            </g:each>
                        </g:if>
                        <g:else>
                            <input type="text" name="o_${outputParamValue.name}_value"/>
                        </g:else>
                    </td>
                </tr>
            </g:each>
        </table>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
        </fieldset>
    </form>
</div>
<script>
    function createTestcase() {
        event.preventDefault();
        var options = {
            target: "#create-testCase"
        };

        $("#createTestcaseForm").ajaxSubmit(options);
    }

</script>