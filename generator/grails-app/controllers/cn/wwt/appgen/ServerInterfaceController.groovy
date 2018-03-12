package cn.wwt.appgen

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ServerInterfaceController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ServerInterface.list(params), model:[serverInterfaceCount: ServerInterface.count()]
    }

    def show(ServerInterface serverInterface) {
        respond serverInterface
    }

    def list(){
        def project = session.getAttribute(ProjectController.PROJECT)
        def apiVersion = session.getAttribute(ProjectController.CUR_API_VERSION)

        def interfaces = ServerInterface.findAllByProjectAndApiVersion(project,apiVersion)
        render(view: "list", model: [interfaces:interfaces])
    }

    def create() {
        respond new ServerInterface(params)
    }

    @Transactional
    def save(ServerInterface serverInterface) {
        if (serverInterface == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (serverInterface.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond serverInterface.errors, view:'create'
            return
        }

        serverInterface.project = session.getAttribute(ProjectController.PROJECT)
        serverInterface.apiVersion = session.getAttribute(ProjectController.CUR_API_VERSION)
        def oldInterface = ServerInterface.findByNameAndProjectAndApiVersion(serverInterface.name,serverInterface.project,serverInterface.apiVersion)
        if(oldInterface){
            serverInterface.getErrors().reject("${serverInterface.name}已经存在了")
            respond serverInterface.errors, view:'create'
            return
        }
        serverInterface.save flush:true

        session.setAttribute(ProjectController.CUR_INTERFACE,serverInterface)
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'serverInterface.label', default: 'ServerInterface'), serverInterface.id])
                redirect serverInterface
            }
            '*' { respond serverInterface, [status: CREATED] }
        }
    }

    def edit(ServerInterface serverInterface) {
        respond serverInterface
    }

    @Transactional
    def update(ServerInterface serverInterface) {
        if (serverInterface == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (serverInterface.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond serverInterface.errors, view:'edit'
            return
        }

        serverInterface.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'serverInterface.label', default: 'ServerInterface'), serverInterface.id])
                redirect serverInterface
            }
            '*'{ respond serverInterface, [status: OK] }
        }
    }

    @Transactional
    def ajaxDel(ServerInterface serverInterface) {
        boolean success = true
        if (serverInterface == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            success = false

        } else{
            InputParam.findAllByServerInterface(serverInterface).each {
                it.delete flush: true
            }
            OutputParam.findAllByServerInterface(serverInterface).each {
                it.delete flush: true
            }
            TestCase.findAllByServerInterface(serverInterface).each {
                it.delete flush: true
            }

            serverInterface.delete flush:true
        }
        render  status: success? "200" : "404"
    }

    def ajaxSelect(ServerInterface serverInterface) {
        if(serverInterface){
            session.setAttribute(ProjectController.CUR_INTERFACE,serverInterface)
            render  status: "200"
        }else {
            render  status: "403"
        }

    }

    @Transactional
    def delete(ServerInterface serverInterface) {

        if (serverInterface == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        serverInterface.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'serverInterface.label', default: 'ServerInterface'), serverInterface.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'serverInterface.label', default: 'ServerInterface'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
