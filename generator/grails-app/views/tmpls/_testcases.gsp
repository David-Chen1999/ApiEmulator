
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
            code:msg
        </th>
        <th width="60px">
            操作
        </th>
    </tr>
    </thead>
    <tbody>
        <g:each in="${testcases}" var="testcase" status="i">
            <tr>
                <td>
                    ${i}
                </td>
                <td>
                    ${testcase.name}
                </td>
                <td>
                    ${testcase.responseCode}:${testcase.responseMsg}
                </td>
                <td>
                    <i style="font-size: 15px;margin-left: 10px" class="glyphicon glyphicon-edit" onclick="editTestcase('${testcase.id}');"></i>
                    <i style="font-size: 15px;margin-lefts: 10px"class="glyphicon glyphicon-trash"  onclick="delTestcase('${testcase.id}');"></i>
                </td>
            </tr>
        </g:each>
    </tbody>
</table>