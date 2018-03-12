package cn.wwt.appgen

/**
 * Created by mlsc on 2017/9/13.
 */
class IOSTemplate {
    public static final String URL_BASE = "let API_URL_BASE = \"\${projectUrlBase}\"";
    public static final String SERVER_INTERFACE_CLASS_TMPL = """
    import Alamofire

    \${innerClass}

    //接口\${serverInterfaceName}的返回对象
    class \${responseClassName} : ServerResponse{
        \${responseClass}
    }

    //\${interfaceDesc}
    func \${methodName}(\${parametersInMethodSignature} callback: (model: \${responseClassName})->Void){ 
        let url = API_URL_BASE + \"/phoneApi/v\${apiVersion}/\${serverInterfaceName}\"
        let parameters = \${paramtersMap}
        Http.\${httpMethod}(url,parameters: parameters){ (httpResponseResult) in
           let response : \${responseClassName} = \${responseClassName}.yy_model(withJSON:httpResponseResult.responseDic)!
           callback(response)
        }
    }
"""

}
