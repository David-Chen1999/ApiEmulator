package cn.wwt.appgen

import cn.wwt.utils.FileUtils
import cn.wwt.utils.StringUtils
import generator.BootStrap
import grails.transaction.Transactional
import org.springframework.util.FileCopyUtils

/**
 * Created by davidchen on 12/04/2017.
 */
@Transactional
class IOSCodeGenerator {

    public static final genCode(Project project, int version, String root) {

        String tmplIOSDir = root + "tmpl/IOS/tmplIOS"
        String targetDir = StringUtils.removeTrailing(project.sourceDir, "/")+ "/src/app/iOS"

        String targetSrcDir = targetDir + "/" + project.name
        String xcodeProjectDir = targetDir + "/" + project.name + ".xcodeproj"
        new File(targetSrcDir).mkdirs()
        new File(xcodeProjectDir).mkdirs()

        copyTemplateFiles(tmplIOSDir, xcodeProjectDir, targetSrcDir, targetDir)

        genSrcFiles(targetSrcDir, project, version)
    }


    static def genSrcFiles(String targetSrcDir, Project project, int version) {
        String constantUrlBase = StringUtils.removeTrailing(project.urlBase, "/");
        FileUtils.makeFile(targetSrcDir, "URLConstants.swift", StringUtils.bind(IOSTemplate.URL_BASE, [projectUrlBase: constantUrlBase]))

        List<ServerInterface> interfaces = ServerInterface.findAllByProjectAndApiVersion(project, version)
        interfaces.each { iface ->
            String responseClassName = StringUtils.firstCharToUpperCase(iface.name) + "Response"
            def binding = [responseClassName  : responseClassName,
                           apiVersion: iface.apiVersion+1,  //TODO version editing
                           serverInterfaceName: iface.name,
                           interfaceDesc      : iface.description,
                           httpMethod         : iface.method.name.toLowerCase(),
                           methodName         : iface.name +"JsonService"]

            List<InputParam> inputParams = InputParam.findAllByServerInterface(iface)
            List<OutputParam> outputParams = OutputParam.findAllByServerInterface(iface)

            genResponseInnerClass(outputParams, binding)

            def parametersInMethodSignature = ""
            inputParams.each {
                parametersInMethodSignature += "${it.name} : ${it.type.toIOSName()}?/*${it.description}*/,\n\t\t"
            }
            binding["parametersInMethodSignature"] = parametersInMethodSignature

            def paramtersMap = "["
            inputParams.each {
                paramtersMap += "\"${it.name}\" : ${it.name},"
            }
            if (paramtersMap.endsWith(",")) {
                paramtersMap = paramtersMap.substring(0, paramtersMap.length() - 1)
            }
            paramtersMap += "]"
            binding["paramtersMap"] = paramtersMap

            String result = StringUtils.bind(IOSTemplate.SERVER_INTERFACE_CLASS_TMPL,binding)

            FileUtils.makeFile(targetSrcDir,StringUtils.firstCharToUpperCase(iface.name)+"API.swift",result)
        }



    }

    public static
    final void genResponseInnerClass(List<OutputParam> outputParams, LinkedHashMap<String, String> binding) {
        def innerClass = " "
        //gen response class
        def responseClass = ""
        outputParams.each {
            switch (it.type.name) {
                case BootStrap.INTEGER:
                    responseClass += "\n        ${it.name} : Int?   // ${it.description}"
                    break
                case BootStrap.LONG:
                    responseClass += "\n        ${it.name} : Int?    // ${it.description}"
                    break
                case BootStrap.FLOAT:
                    responseClass += "\n        ${it.name} : Float?    // ${it.description}"
                    break
                case BootStrap.DOUBLE:
                    responseClass += "\n        ${it.name} : Double?    // ${it.description}"
                    break
                case BootStrap.BIG_DECIMAL:
                    responseClass += "\n        ${it.name}: Decimal?    // ${it.description}"
                    break
                case BootStrap.PASSWORD:
                case BootStrap.STRING:
                    responseClass += "\n        ${it.name} : String?    // ${it.description}"
                    break
                case BootStrap.DATE:
                    responseClass += "\n        ${it.name} : Date?    // ${it.description}"
                    break
                case BootStrap.ARRAY:
                    def innerClassName = StringUtils.firstCharToUpperCase(it.name) + "Field"
                    responseClass += "\n       ${it.name} : Array<${innerClassName}>?    // ${it.description}"
                    innerClass += "\n\n//inner class in response class, ${it.description} \nclass ${innerClassName} : NSObject {"
                    it.fields.each {
                        switch (it.type.name) {
                            case BootStrap.INTEGER:
                                innerClass += "\n        ${it.name} : Int?// ${it.description}"
                                break
                            case BootStrap.LONG:
                                innerClass += "\n        ${it.name} : Int?// ${it.description}"
                                break
                            case BootStrap.FLOAT:
                                innerClass += "\n        ${it.name} : Float?// ${it.description}"
                                break
                            case BootStrap.DOUBLE:
                                innerClass += "\n        ${it.name} : Double?// ${it.description}"
                                break
                            case BootStrap.BIG_DECIMAL:
                                innerClass += "\n        ${it.name} : Decimal?// ${it.description}"
                                break
                            case BootStrap.PASSWORD:
                            case BootStrap.STRING:
                                innerClass += "\n        ${it.name} : String?// ${it.description}"
                                break
                            case BootStrap.DATE:
                                innerClass += "\n        ${it.name} : Date?// ${it.description}"
                                break
                        }
                    }
                    innerClass += "\n       }"
                    break
                case BootStrap.OBJECT:
                    def innerClassName = StringUtils.firstCharToUpperCase(it.name) + "Field"
                    responseClass += "\n        ${it.name} : ${innerClassName}? // ${it.description}"
                    innerClass += "     class ${innerClassName} : NSObject{"
                    it.fields.each {
                        switch (it.type.name) {
                            case BootStrap.INTEGER:
                                innerClass += "\n        ${it.name} : Int?// ${it.description}"
                                break
                            case BootStrap.LONG:
                                innerClass += "\n        ${it.name} : Int?// ${it.description}"
                                break
                            case BootStrap.FLOAT:
                                innerClass += "\n        ${it.name} : Float?// ${it.description}"
                                break
                            case BootStrap.DOUBLE:
                                innerClass += "\n        ${it.name} : Double?// ${it.description}"
                                break
                            case BootStrap.BIG_DECIMAL:
                                innerClass += "\n        ${it.name} : Decimal?// ${it.description}"
                                break
                            case BootStrap.PASSWORD:
                            case BootStrap.STRING:
                                innerClass += "\n        ${it.name} : String?// ${it.description}"
                                break
                            case BootStrap.DATE:
                                innerClass += "\n        ${it.name} : Date?// ${it.description}"
                                break
                        }
                    }
                    innerClass += "\n       }"
                    break

//                        BootStrap.FILE
//                        BootStrap.IMAGE
//                        BootStrap.TAKE_PIC
//                        BootStrap.PICK_PIC
//                        BootStrap.VIDEO
//                        BootStrap.TAKE_VIDEO
//                        BootStrap.LOCATION
//                        BootStrap.ARRAY
            }
        }

        binding.put("innerClass", innerClass)
        binding.put("responseClass", responseClass)
    }

    private static void copyTemplateFiles(String tmplIOSDir, String xcodeProjectDir, String srcDir, String targetDir) {
        FileUtils.copyDir(tmplIOSDir + "/project.xcodeproj", xcodeProjectDir)
        FileUtils.copyDir(tmplIOSDir + "/project", srcDir)
        FileCopyUtils.copy(new File(tmplIOSDir + "/README.md"), new File(targetDir + "/README.md"))
    }

}
