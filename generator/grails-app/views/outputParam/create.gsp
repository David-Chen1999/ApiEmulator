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
    <form method="post" id="createOutputForm" action="<g:createLink controller="outputParam" action="save" />" onsubmit="createOutput();">
        <fieldset class="form">
            <f:field property="name" bean="outputParam"/>
            <f:field property="type" bean="outputParam"/>
            <f:field property="description" bean="outputParam"/>

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
                <g:each in="${1..cn.wwt.appgen.OutputParamController.MAX_FIELD_NUM}" var="i" status="rowI">
                    <tr>
                        <td>
                            <input type="text" name="row_${i}_name">
                        </td>
                        <td>
                            %{--<f:field name="row_1_type" property="type" bean="outputParam"/>--}%
                            <select name="row_${i}_type.id" id="row_${i}_type" style="height: 28px">
                                <option value="null"></option>
                                <option value="1">Integer:1</option>
                                <option value="2">String:2</option>
                                <option value="3">Password:3</option>
                                <option value="4">Long:4</option>
                                <option value="5">Float:5</option>
                                <option value="6">Double:6</option>
                                <option value="7">BigDecimal:7</option>
                                <option value="8">Date:8</option>
                                <option value="9">File:9</option>
                                <option value="10">Image:10</option>
                                <option value="11">TakePic:11</option>
                                <option value="12">PickPic:12</option>
                                <option value="13">Video:13</option>
                                <option value="14">TakeVideo:14</option>
                                <option value="15">Location:15</option>
                                %{--<option value="16">Array:16</option>--}%
                                %{--<option value="17">Object:17</option>--}%
                            </select>
                        </td>
                        <td>
                            <input type="text" name="row_${i}_description">
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
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