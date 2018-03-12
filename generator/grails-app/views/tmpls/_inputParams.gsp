
<table class="table">
    <thead>
    <tr>
        <th>
            #
        </th>
        <th>
            名称
        </th>
        <th>
            类型
        </th>
        <th>
            描述
        </th>
        <th width="60px">
            操作
        </th>
    </tr>
    </thead>
    <tbody>
        <g:each in="${inputParams}" var="param" status="i">
            <tr>
                <td>
                    ${i}
                </td>
                <td>
                    ${param.name}
                </td>
                <td>
                    ${param.type.name}
                </td>
                <td>
                    ${param.description}
                </td>
                <td>
                    <i style="font-size: 15px;margin-left: 10px" class="glyphicon glyphicon-edit" onclick="editInputParam('${param.id}');"></i>
                    <i style="font-size: 15px;margin-lefts: 10px"class="glyphicon glyphicon-trash"  onclick="delInputParam('${param.id}');"></i>
                </td>
            </tr>
        </g:each>
    </tbody>
</table>