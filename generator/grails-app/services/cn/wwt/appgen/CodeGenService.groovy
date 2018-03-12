package cn.wwt.appgen

import grails.transaction.Transactional

import javax.servlet.http.HttpSession

@Transactional
class CodeGenService {

    public static final String GEN_PROGRESS = "genProgress"
    public static final String GEN_MSG = "genMsg"

    def genCode(Project project, int version, String root, HttpSession session) {
        AndroidCodeGenerator.genCode(project, version, root)
        session.setAttribute(GEN_PROGRESS,25);
        session.setAttribute(GEN_MSG,"Android generated");
        WeAppCodeGenerator.genCode(project, version, root)
        session.setAttribute(GEN_PROGRESS,50);
        session.setAttribute(GEN_MSG,"Wechat app generated");
        IOSCodeGenerator.genCode(project, version, root)
        session.setAttribute(GEN_PROGRESS,75);
        session.setAttribute(GEN_MSG,"iOS app generated");
        ServerCodeGenerator.genCode(project, version, root)
        session.setAttribute(GEN_PROGRESS,100);
        session.setAttribute(GEN_MSG,"All genearated");
    }

    private mergeTmpl(URL url, Map binding, String targetFile) {
        def engine = new groovy.text.GStringTemplateEngine()
        def template = engine.createTemplate(url).make(binding)
        new File(targetFile).withPrintWriter { writer ->
            writer.write(template.toString())
        }
    }

}
