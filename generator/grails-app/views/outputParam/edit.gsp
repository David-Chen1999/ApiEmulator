<%@ page import="cn.wwt.appgen.model.JavaType" %>
<div id="create-outputParam" class="content scaffold-create" role="main">
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${this.outputParam}">
        <ul class="errors" role="alert">
            <g:eachError bean="${this.outputParam}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <form method="POST" id="createOutputForm" action="<g:createLink controller="outputParam" action="ajaxEdit" />/${outputParam.id}" onsubmit="createOutput();">
        <fieldset class="form">
            <f:field property="name" bean="outputParam"/>
            <f:field property="type" bean="outputParam"/>
        </fieldset>
        <table id="sub_fields_table">
            <thead>
            <tr>
                <th>name</th>
                <th>type</th>
                <th>description</th>
            </tr>
            </thead>
            <tbody style="overflow: scroll">
            <%
                def list =JavaType.list()
             %>
            <g:each in="${1..cn.wwt.appgen.OutputParamController.MAX_FIELD_NUM}" var="i">
                <g:if test="${i<=outputParam.fields.size()}">
                    <g:set var="field" value="${outputParam.fields[i-1]}"/>
                </g:if>
                <g:else>
                    <g:set var="field" value="${new cn.wwt.appgen.OutputParam()}"/>
                </g:else>
                <tr>
                    <td>
                        <input type="text" name="row_${i}_name" value="${field?.name}">
                    </td>
                    <td>
                        <select name="row_${i}_type.id" id="row_${i}_type" style="height: 28px">
                            <%
                                for(int j =0 ;j<list.size();j++){
                                    def type = list[j]
                                    def selected = field?.type == type ? "selected='selected'": ""
                                    out<< "<option value='${type.id}'  ${selected}>${type.name}</option>"
                                }
                            %>
                        </select>
                    </td>
                    <td>
                        <input type="text" name="row_${i}_description" value="${field?.description}">
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="更新" />
        </fieldset>
    </form>
</div>
<script>
    function createOutput() {
        event.preventDefault();
        var options = {
            target: "#create-outputParam"
        };

        $("#createOutputForm").ajaxSubmit(options);
    }

</script>