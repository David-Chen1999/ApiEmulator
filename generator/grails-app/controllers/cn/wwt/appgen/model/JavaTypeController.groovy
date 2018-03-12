package cn.wwt.appgen.model

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class JavaTypeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond JavaType.list(params), model:[javaTypeCount: JavaType.count()]
    }

    def show(JavaType javaType) {
        respond javaType
    }

    def create() {
        respond new JavaType(params)
    }

    @Transactional
    def save(JavaType javaType) {
        if (javaType == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (javaType.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond javaType.errors, view:'create'
            return
        }

        javaType.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'javaType.label', default: 'JavaType'), javaType.id])
                redirect javaType
            }
            '*' { respond javaType, [status: CREATED] }
        }
    }

    def edit(JavaType javaType) {
        respond javaType
    }

    @Transactional
    def update(JavaType javaType) {
        if (javaType == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (javaType.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond javaType.errors, view:'edit'
            return
        }

        javaType.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'javaType.label', default: 'JavaType'), javaType.id])
                redirect javaType
            }
            '*'{ respond javaType, [status: OK] }
        }
    }

    @Transactional
    def delete(JavaType javaType) {

        if (javaType == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        javaType.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'javaType.label', default: 'JavaType'), javaType.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'javaType.label', default: 'JavaType'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
