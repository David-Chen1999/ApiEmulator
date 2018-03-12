package cn.wwt.appgen

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class InputParamController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond InputParam.list(params), model:[inputParamCount: InputParam.count()]
    }

    def show(InputParam inputParam) {
        respond inputParam
    }

    def create() {
        respond new InputParam(params)
    }

    @Transactional
    def save(InputParam inputParam) {
        if (inputParam == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        ServerInterface serverInterface = session.getAttribute(ProjectController.CUR_INTERFACE)
        if(!serverInterface){
            inputParam.getErrors().reject("404","没有指定接口")
            respond inputParam.errors, view:'create'
            return
        }

        if (inputParam.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond inputParam.errors, view:'create'
            return
        }

        def existingParam = InputParam.findByServerInterfaceAndName(serverInterface,inputParam.name)
        if(existingParam){
            inputParam.getErrors().reject("403","${inputParam.name}已经存在了")
            respond inputParam.errors, view:'create'
            return
        }

        inputParam.serverInterface = serverInterface
        inputParam.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'inputParam.label', default: 'InputParam'), inputParam.id])
                redirect inputParam
            }
            '*' { respond inputParam, [status: CREATED] }
        }
    }

    def edit(InputParam inputParam) {
        respond inputParam
    }

    def ajaxEdit(InputParam inputParam){
        update(inputParam)
    }
    @Transactional
    def update(InputParam inputParam) {
        if (inputParam == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (inputParam.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond inputParam.errors, view:'edit'
            return
        }

        inputParam.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'inputParam.label', default: 'InputParam'), inputParam.id])
                redirect inputParam
            }
            '*'{ respond inputParam, [status: OK] }
        }
    }

    @Transactional
    def delete(InputParam inputParam) {

        if (inputParam == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        inputParam.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'inputParam.label', default: 'InputParam'), inputParam.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'inputParam.label', default: 'InputParam'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Transactional
    def ajaxDel(InputParam dataItem){
        boolean success = true
        if (dataItem == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            success = false

        } else{
            //also delete test case input value
            def type = InputParamValue.findByType(dataItem)
            type*.each{
                def testCase = it.testCase
                testCase.inputs.remove(it)
                it.delete()
                testCase.save()

            }
            dataItem.delete() // flush:true


        }
        render  status: success? "200" : "404"
    }

    @Transactional
    def listInputs() {
        ServerInterface curInterface = session.getAttribute(ProjectController.CUR_INTERFACE)
        if(!curInterface){
            render(view: "list",model: [inputParams:[]])
        }
        def inputParams = InputParam.findAllByServerInterface(curInterface)

        render(view: "list",model: [inputParams:inputParams])
    }


}
