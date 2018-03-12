package cn.wwt.appgen

import cn.wwt.utils.FileUtils
import cn.wwt.utils.StringUtils
import generator.BootStrap
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import groovy.text.SimpleTemplateEngine

/**
 * Created by davidchen on 12/04/2017.
 */
@Transactional
class ServerCodeGenerator {
    public static final urlMappingTmpl = """
package server

class UrlMappings {

    static mappings = {
        "/\\\$controller/\\\$action?/\\\$id?(.\\\$format)?"{
            constraints {
                // apply constraints here
            }
        }
        \${mappings}
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
"""

    def static mappingItemTmpl ="\"/phoneApi/v\${version}/\\\$action?\"(controller: \"PhoneApiV\${version}\")"

    def static controllerTmpl = """
package \${packageName}

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.text.SimpleDateFormat
import org.apache.commons.collections.map.LRUMap
@Transactional(readOnly = true)
class \${controllerName}Controller {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static final LRUMap userCache = new LRUMap(1000);
\${innerClasses}
\${methods}

    public static Map validateDate(String date){
        if(date != null){
            try {
                Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(date)
            }catch (RuntimeException e){
                def result = [:]
                result.put("success",false)
                result.put("msg",date + "can't convert to Date")
                return result
            }
        }
        return null
    }

    public static Map validateFloat(String floatStr){
        if(floatStr != null){
            try {
                Float.parseFloat(floatStr)
            }catch (RuntimeException e){
                def result = [:]
                result.put("success",false)
                result.put("msg",floatStr + "can't convert to Float")
                return result
            }
        }
        return null
    }

    public static Map validateLong(String longStr){
        if(longStr != null){
            try {
                Long.parseLong(longStr)
            }catch (RuntimeException e){
                def result = [:]
                result.put("success",false)
                result.put("msg",longStr + "can't convert to Float")
                return result
            }
        }
        return null
    }
}
"""
    public static final methodBegin ="""
   /**
     * \${description}
     * inputs:\${inputs}
     * outputs:\${outputs}
     * @return
     */
   def \${name}(){
"""

    public static final String validation = """
    result = validate\${type}(\${inputName}Str)
    if(result){
        render result as JSON
        return
    }
"""

    public static final genCode(Project project, int version, String root) {

        String tmplDir = root + "/tmpl/grails"
        String targetDir = StringUtils.removeTrailing(project.sourceDir, "/")
        def targetServerRoot = targetDir + "/src/server"
        String mappingFileDir = targetServerRoot + "/grails-app/controllers/server"
        String controllerDir = targetServerRoot + "/grails-app/controllers"
        String domainClassDir = targetServerRoot + "/grails-app/domain"

        FileUtils.copyDir(tmplDir, targetServerRoot)

        def mappingItemContent = StringUtils.bind(mappingItemTmpl, [version: version])
        def urlMappingContent = StringUtils.bind(ServerCodeGenerator.urlMappingTmpl, [mappings: mappingItemContent])
        FileUtils.makeFile(mappingFileDir,"UrlMappings.groovy", urlMappingContent)

        def controllerName = "PhoneApiV"+version
        List<ServerInterface> interfaces = ServerInterface.findAllByProjectAndApiVersion(project, version)
        def methods =""
        def innerClasses = genInnerClass(interfaces)
        interfaces.each { iface ->
            def inputs = ""
            iface.inputParams.each {
                inputs += "\n           ${it.type.name}:    ${it.name}, "
            }

            def outputs = ""
            iface.outputParams.each {
                outputs += "\n          ${it.type.name}:    ${it.name}"
            }

            methods += StringUtils.bind(methodBegin,[name:iface.name,description:iface.description,inputs:inputs,outputs:outputs])
            //handle input parameters
            iface.inputParams.each { input->
                methods += "\n    def ${input.name}Str = params.${input.name}"
            }
            methods += "\n    def result = null\n"

            methods += "\n\n    //Validate inputs\n"
            iface.inputParams.each { inputParam->
                switch (inputParam.type.name) {
                    case BootStrap.DATE:
                        methods += StringUtils.bind(validation,[inputName:inputParam.name, type:"Date"])
                        break
                    case BootStrap.DATE_TIME:
                        methods += StringUtils.bind(validation, [inputName: inputParam.name,type:"Date"])
                        break
                    case BootStrap.LONG:
                    case BootStrap.INTEGER:
                        methods += StringUtils.bind(validation, [inputName: inputParam.name,type:"Long"])
                        break
                    case BootStrap.FLOAT:
                    case BootStrap.DOUBLE:
                        methods += StringUtils.bind(validation, [inputName: inputParam.name,type:"FLOAT"])
                        break
                }
            }

            methods += "\n\n    //TODO logic process here\n\n"

            //handle test case
            iface.testCases.each { test->
                methods += "\n   if( true"
                test.inputs.each { inVal ->
                    methods += " && ${inVal.type.name}Str == \"${inVal.value}\" "
                }

                methods += " ){"
                //output
                methods += "\n      result = [:]"
                methods += "\n      result.put(\"success\",${test.success})"
                methods += "\n      result.put(\"code\",\"${test.responseCode}\")"
                methods += "\n      result.put(\"msg\",\"${test.responseMsg}\")"

                test.outputs.each {outVal ->
                    if(outVal.type.type.name == BootStrap.OBJECT || outVal.type.type.name == BootStrap.ARRAY){
//                        int firstPos = ((String)outVal).indexOf("}")
//                        int lastPos = ((String)outVal).lastIndexOf("}")
                        String valStr = outVal.value.replaceAll('\\{','[').replaceAll('}',']')//TODO last replacement is not safe
                        methods += "\n      result.put(\"${outVal.type.name}\",${valStr})"
                    } else{
                        methods += "\n      result.put(\"${outVal.type.name}\",\"${outVal.value}\")"
                    }


                }

                methods += "\n      render result as JSON\n      return\n"
                methods += "\n      }"
            }
            methods += "\n    }"
        }
            //method ,input parameters/ gen output

        def classContent = StringUtils.bind(controllerTmpl, [methods: methods, innerClasses: innerClasses, packageName: project.packageName, controllerName: controllerName])
//        FileUtils.makeFile(controllerDir,controllerName+"Controller.groovy", classContent)
        FileUtils.makeClass(controllerDir,project.packageName,controllerName+"Controller.groovy",classContent)
    }

    private static String genInnerClass(List<ServerInterface> interfaces) {
        def innerClasses = ""
        def HashSet<String> generatedInnerClasses = new HashSet<>()
        interfaces.each { iface ->
            iface.outputParams.each {
                if (BootStrap.ARRAY == it.type.name || BootStrap.OBJECT == it.type.name) {
                    def innerClassName = StringUtils.firstCharToUpperCase(it.name) + "Field"
                    if (generatedInnerClasses.contains(innerClassName)) {
                        return
                    }

                    innerClasses += "\n   public static class ${innerClassName}{"
                    it.fields.each {
                        switch (it.type.name) {
                            case BootStrap.INTEGER:
                                innerClasses += "\n        Integer ${it.name};\n"
                                break
                            case BootStrap.LONG:
                                innerClasses += "\n        Long ${it.name};\n"
                                break
                            case BootStrap.FLOAT:
                                innerClasses += "\n        Float ${it.name};\n"
                                break
                            case BootStrap.DOUBLE:
                                innerClasses += "\n        Double ${it.name};\n"
                                break
                            case BootStrap.BIG_DECIMAL:
                                innerClasses += "\n        BigDecimal ${it.name};\n"
                                break
                            case BootStrap.PASSWORD:
                            case BootStrap.STRING:
                                innerClasses += "\n        String ${it.name};\n"
                                break
                            case BootStrap.DATE:
                                innerClasses += "\n        Date ${it.name};\n"
                                break

                            case BootStrap.TIME:
                                innerClasses += "\n        String ${it.name};\n"
                                break

                            case BootStrap.DATE_TIME:
                                innerClasses += "\n        Date ${it.name};\n"
                                break
                        }
                    }
                    innerClasses += "\n       }"
                }
            }
        }
        return innerClasses
    }

}
