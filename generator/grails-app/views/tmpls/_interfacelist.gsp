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
            描述
        </th>
        <th width="60px">
            操作
        </th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${interfaces}" var="sinterface" status="i">
        <tr id="interface${i}" onclick="selectRow('interface${i}','${sinterface.id}');">
            <td width="20%">
                ${i+1}
            </td>
            <td>
                ${sinterface.name}
            </td>
            <td>
                ${sinterface.description}
            </td>
            <td >

                <i style="font-size: 15px;margin-lefts: 10px"class="glyphicon glyphicon-trash pull-right"  onclick="delInterface('${sinterface.id}');"></i>
                <i style="font-size: 15px;margin-left: 10px" class="glyphicon glyphicon-edit pull-right" onclick="editInterface('${sinterface.id}');"></i>
            </td>
        </tr>
    </g:each>

    </tbody>
</table>