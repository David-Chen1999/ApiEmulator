<div id="show-testCase" class="content scaffold-show" role="main">
    <h1>创建成功</h1>
    <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:showBean bean="testCase" properties="['name','responseCode','responseMsg','description']"/>
    <h2>输入值</h2>
    <table>
        <thead>
        <tr>
            <th>name</th><th>type</th><th>value</th></tr>
        </thead>
        <g:each in="${testCase.inputs}" var="input">
            <tr>
                <td>${input.type.name}</td><td>${input.type.type}</td>
                <td>${input.value}</td>
            </tr>
        </g:each>
    </table>
    <h2>输出值</h2>
    <table>
        <thead>
        <tr>
            <th>name</th><th>type</th><th>value</th></tr>
        </thead>
        <g:each in="${testCase.outputs}" var="output">
            <tr>
                <td>${output.type.name}</td><td>${output.type.type}</td>
                <td>
                    ${output.value}
                </td>
            </tr>
        </g:each>
    </table>
    </div>
