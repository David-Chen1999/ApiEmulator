<div id="show-dataItem" class="content scaffold-show" role="main">
    <h1>创建/更新成功</h1>
    <g:showBean bean="outputParam" properties="['name','type','description']"/>
    <h2>子属性</h2>
    <table id="sub_fields_table">
        <thead>
        <tr>
            <th>name</th>
            <th>type</th>
            <th>description</th>
        </tr>
        </thead>
        <tbody style="overflow: scroll">

        <g:each in="${outputParam.fields}" var="field" status="i">
            <tr>
                <td>
                    ${field.name}
                </td>
                <td>
                    ${field.type}
                </td>
                <td>
                    ${field.description}
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>
