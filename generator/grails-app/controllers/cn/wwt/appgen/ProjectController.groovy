package cn.wwt.appgen

import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ProjectController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    public static final String PROJECT = "project"
    public static final String CUR_API_VERSION = "curVersion"
    public static final String CUR_INTERFACE = "curInterface"
    def codeGenService

    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        def list = Project.list(params)
        Project project = new Project()
        if(list.size() > 0){
            project = list.first()
        }else {
            redirect(action: "create")
            return
        }
        show(project,list)
    }

    def ajaxListAppPages(Project project){
        render(model: [pageList: project.appPages], template: "/tmpls/pageList")
    }

    def show(Project project,List<Project> list) {
        if(list == null){
            list = Project.list()
        }

        List apiVersions = ServerInterface.executeQuery("select distinct s.apiVersion from ServerInterface s where s.project = ? order by s.apiVersion desc",[project])
        if(apiVersions.size()<1){
            apiVersions.addAll([1])
        }
        Integer curVersion = apiVersions.max()

        session.setAttribute(PROJECT,project)
        session.setAttribute(CUR_API_VERSION,curVersion)

        def interfaces = ServerInterface.findAllByProjectAndApiVersion(project,curVersion)

        def model = [list: list,interfaces:interfaces, apiVersions: apiVersions, curVersion: curVersion, project: project, projectCount: Project.count()]
        if(interfaces.size() >0){
            def curInterface = interfaces.first()
            def inputParams = InputParam.findAllByServerInterface(curInterface)

            def outputParams = OutputParam.findAllByServerInterface(curInterface)

            model.put(CUR_INTERFACE,curInterface)
            model.put("inputParams",inputParams)

            model.put("outputParams",outputParams)

            session.setAttribute(CUR_INTERFACE,curInterface)

            def testcases = TestCase.findAllByServerInterface(curInterface)
            model.put("testcases",testcases)
        }

        render(model: model,view:"index" )
    }

    def ajaxGen(){
        def project = session.getAttribute(PROJECT)
        def curVersion = session.getAttribute(CUR_API_VERSION)
        String root= request.getServletContext().getRealPath("/")
        session.setAttribute(CodeGenService.GEN_PROGRESS,0);
        session.setAttribute(CodeGenService.GEN_MSG,"start...");
        def curSession = session;
        new Thread(){
            @Override
            void run() {
                codeGenService.genCode(project,curVersion,root,curSession)
            }
        }.start()
        render status:"200"
    }
    def create() {
        def list = Project.list(params)
        render(model: [project:new Project(params), list: list, projectCount: Project.count()],view:"create" )
    }

    def genProgress(){
        String msg = session.getAttribute(CodeGenService.GEN_MSG)
        String percent = session.getAttribute(CodeGenService.GEN_PROGRESS)
        def result = [msg:msg, percent:percent]
        render result as JSON
    }
    @Transactional
    def save(Project project) {
        if (project == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (project.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond project.errors, view:'create'
            return
        }

        project.save flush:true

//        request.withFormat {
//            form multipartForm {
//                flash.message = message(code: 'default.created.message', args: [message(code: 'project.label', default: 'Project'), project.id])
//                redirect project
//            }
//            '*' { redirect index }
//        }
        redirect(action: "index")
    }

    def edit(Project project) {
        def list = Project.list(params)
        render(model: [project:project, list: list, projectCount: Project.count()],view:"edit" )
    }

    @Transactional
    def update(Project project) {
        if (project == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (project.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond project.errors, view:'edit'
            return
        }

        project.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'project.label', default: 'Project'), project.id])
                redirect project
            }
            '*'{ respond project, [status: OK] }
        }
    }

    @Transactional
    def delete(Project project) {

        if (project == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        project.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'project.label', default: 'Project'), project.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
