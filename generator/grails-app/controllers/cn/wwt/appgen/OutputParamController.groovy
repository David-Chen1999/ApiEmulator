package cn.wwt.appgen

import cn.wwt.appgen.model.JavaType
import generator.BootStrap

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class OutputParamController {
    public static final int MAX_FIELD_NUM = 15
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond OutputParam.list(params), model:[outputParamCount: OutputParam.count()]
    }

    def show(OutputParam outputParam) {
        respond outputParam
    }

    def create() {
        respond new OutputParam(params)
    }

    @Transactional
    def save(OutputParam outputParam) {
        if (outputParam == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        ServerInterface serverInterface = session.getAttribute(ProjectController.CUR_INTERFACE)
        if(!serverInterface){
            outputParam.getErrors().reject("404","没有指定接口")
            respond outputParam.errors, view:'create'
            return
        }
        if (outputParam.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond outputParam.errors, view:'create'
            return
        }

        if(OutputParam.findByServerInterfaceAndName(serverInterface,outputParam.name)){
            outputParam.getErrors().reject("403","${outputParam.name}已经存在了")
            respond outputParam.errors, view:'create'
            return
        }
        def arrayType = JavaType.findByName(BootStrap.ARRAY)
        def objectType = JavaType.findByName(BootStrap.OBJECT)
        if(arrayType == outputParam.type || objectType == outputParam.type ){
            for(int i =1 ; i <= MAX_FIELD_NUM; i++ ){
                String name = params["row_${i}_name"]
                String type = params["row_${i}_type.id"]
                String description = params["row_${i}_description"]
                if(name && type){
                    JavaType javaType = JavaType.read(type as Long)
                    OutputParam tmp = new OutputParam(name:name,type:javaType,description: description)
                    outputParam.addToFields(tmp)
                }
            }
        }

        outputParam.serverInterface = serverInterface
        outputParam.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'outputParam.label', default: 'OutputParam'), outputParam.id])
                redirect outputParam
            }
            '*' { respond outputParam, [status: CREATED] }
        }
    }

    @Transactional
    def edit(OutputParam outputParam) {
        outputParam.attach()
        respond outputParam
    }

    @Transactional
    def update(OutputParam outputParam) {
        if (outputParam == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (outputParam.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond outputParam.errors, view:'edit'
            return
        }
        outputParam.fields.clear()

        def arrayType = JavaType.findByName(BootStrap.ARRAY)
        def objectType = JavaType.findByName(BootStrap.OBJECT)
        if(arrayType == outputParam.type || objectType == outputParam.type ){
            for(int i =1 ; i <= MAX_FIELD_NUM; i++ ){
                String name = params["row_${i}_name"]
                String type = params["row_${i}_type.id"]
                String desc = params["row_${i}_description"]
                if(name && type){
                    JavaType javaType = JavaType.read(type as Long)
                    OutputParam tmp = new OutputParam(name:name,type:javaType,description: desc)
                    outputParam.addToFields(tmp)
                }
            }
        }
        outputParam.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'outputParam.label', default: 'OutputParam'), outputParam.id])
                redirect outputParam
            }
            '*'{ respond outputParam, [status: OK] }
        }
    }

    def ajaxEdit(OutputParam outputParam){
        update(outputParam)
    }

    @Transactional
    def delete(OutputParam outputParam) {

        if (outputParam == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        outputParam.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'outputParam.label', default: 'OutputParam'), outputParam.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'outputParam.label', default: 'OutputParam'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Transactional
    def listOutputs(){
        ServerInterface curInterface = session.getAttribute(ProjectController.CUR_INTERFACE)
        if(!curInterface){
            render(view: "list",model: [outputParams:[]])
        }
        def outputParams = OutputParam.findAllByServerInterface(curInterface)

        render(view: "list",model: [outputParams:outputParams])
    }

    @Transactional
    def ajaxDel(OutputParam dataItem){
        boolean success = true
        if (dataItem == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            success = false

        } else{
            dataItem.delete() // flush:true
        }
        render  status: success? "200" : "404"
    }
}
