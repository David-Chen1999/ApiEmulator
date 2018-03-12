package cn.wwt.appgen

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ServerResponseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ServerResponse.list(params), model:[serverResponseCount: ServerResponse.count()]
    }

    def show(ServerResponse serverResponse) {
        respond serverResponse
    }

    def create() {
        respond new ServerResponse(params)
    }

    @Transactional
    def save(ServerResponse serverResponse) {
        if (serverResponse == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (serverResponse.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond serverResponse.errors, view:'create'
            return
        }

        serverResponse.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'serverResponse.label', default: 'ServerResponse'), serverResponse.id])
                redirect serverResponse
            }
            '*' { respond serverResponse, [status: CREATED] }
        }
    }

    def edit(ServerResponse serverResponse) {
        respond serverResponse
    }

    @Transactional
    def update(ServerResponse serverResponse) {
        if (serverResponse == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (serverResponse.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond serverResponse.errors, view:'edit'
            return
        }

        serverResponse.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'serverResponse.label', default: 'ServerResponse'), serverResponse.id])
                redirect serverResponse
            }
            '*'{ respond serverResponse, [status: OK] }
        }
    }

    @Transactional
    def delete(ServerResponse serverResponse) {

        if (serverResponse == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        serverResponse.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'serverResponse.label', default: 'ServerResponse'), serverResponse.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'serverResponse.label', default: 'ServerResponse'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
