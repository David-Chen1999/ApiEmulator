package cn.wwt.appgen

import cn.wwt.utils.StringUtils
import generator.BootStrap
import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TestcaseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TestCase.list(params), model:[TestCaseCount: TestCase.count()]
    }

    def show(TestCase testcase) {
        respond testcase
    }

    def create() {
        ServerInterface curInterface = session.getAttribute(ProjectController.CUR_INTERFACE)
        def inputParams = InputParam.findAllByServerInterface(curInterface)

        def outputParams = OutputParam.findAllByServerInterface(curInterface)

        respond new TestCase(params), model:[inputs:inputParams, outputs:outputParams]
    }

    @Transactional
    def save(TestCase testcase) {
        ServerInterface curInterface = session.getAttribute(ProjectController.CUR_INTERFACE)
        def inputParams = InputParam.findAllByServerInterface(curInterface)
        def outputParams = OutputParam.findAllByServerInterface(curInterface)

        if (testcase == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (testcase.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond testcase.errors, view:'create', model:[inputs:inputParams, outputs:outputParams]
            return
        }

        ServerInterface serverInterface = session.getAttribute(ProjectController.CUR_INTERFACE)
        if(!serverInterface){
            testcase.getErrors().reject("404","没有指定接口")
            respond testcase.errors, view:'create'
            return
        }

        if(TestCase.findByServerInterfaceAndName(serverInterface,testcase.name)){
            testcase.getErrors().reject("403","${testcase.name}已经存在了")
            respond testcase.errors, view:'create', model:[inputs:inputParams, outputs:outputParams]
            return
        }

        //process input
        testcase.inputs?.clear()
        inputParams.each {p->
            String inputName = "i_${p.name}_value"
            def val = params[inputName]
            if(val){
                testcase.addToInputs(new InputParamValue(testcase:testcase,type: p,value: val ))
            }
        }
        //process output
        testcase.outputs?.clear()
        outputParams.each { p->
            if(p.type.name == BootStrap.OBJECT ){
                Map keyVals = [:]
                p.fields.each {f->
                    String key = "o_${p.name}_${f.name}_value"
                    String val = params[key]
                    if(val){
                        keyVals.put(f.name,val)
                    }
                }
                if(keyVals){
                    String val = keyVals as JSON
                    testcase.addToOutputs(new OutputParamValue(testcase: testcase,type: p,value: val))
                }
            } else if(p.type.name == BootStrap.ARRAY){
                Map keyVals = [:]
                int total = 0
                int startStep = 0
                int endStep = 0
                p.fields.each {f->
                    String key = "o_${p.name}_${f.name}_value"
                    String val = params[key]
                    if(val){
                        def m = val =~ /\[(\d+)..(\d+)\]/
                        if (m) {
                            def match = m.group(0)
                            int first = m.group(1) as Integer
                            int end = m.group(2) as Integer
                            startStep = first
                            endStep = end
                            if((end - first) > total){
                                total = end -first
                            }
                            val = ((String)val).replace(match,"\${num}")
                        }

                        keyVals.put(f.name,val)

                    }
                }

                if(keyVals){
                    String val = keyVals as JSON
                    def totalVal ="["
                    def range = startStep..endStep
                    range.each {
                        totalVal += StringUtils.bind(val,[num:it]) +","
                    }
                    totalVal = StringUtils.removeTrailing(totalVal,",")
                    totalVal += "]"
                    testcase.addToOutputs(new OutputParamValue(testcase: testcase,type: p,value: totalVal))
                }
            }   else {
                String outputName = "o_${p.name}_value"
                def val = params[outputName]
                if(val){
                    testcase.addToOutputs(new OutputParamValue(testcase:testcase,type: p,value: val ))
                }
            }

        }

        testcase.serverInterface = serverInterface
        testcase.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'TestCase.label', default: 'TestCase'), testcase.id])
                redirect testcase
            }
            '*' { respond testcase, [status: CREATED] }
        }
    }

    def edit(TestCase testcase) {
        respond testcase
    }

    @Transactional
    def update(TestCase testcase) {
        if (testcase == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (testcase.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond testcase.errors, view:'edit'
            return
        }

        testcase.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'TestCase.label', default: 'TestCase'), testcase.id])
                redirect testcase
            }
            '*'{ respond testcase, [status: OK] }
        }
    }

    @Transactional
    def delete(TestCase testcase) {

        if (testcase == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        testcase.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TestCase.label', default: 'TestCase'), testcase.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'TestCase.label', default: 'TestCase'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def listTestcases(){
        ServerInterface curInterface = session.getAttribute(ProjectController.CUR_INTERFACE)
        if(!curInterface){
            render(view: "list",model: [outputParams:[]])
            return
        }
        def testcases = TestCase.findAllByServerInterface(curInterface)

        render(view: "list",model: [testcases:testcases])
    }

    @Transactional
    def ajaxDel(TestCase testcase){
        boolean success = true
        if (testcase == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            success = false

        } else{
            InputParamValue.findAllByTestCase(testcase).each {
                it.delete()
            }

            OutputParamValue.findAllByTestCase(testcase).each{
                it.delete()
            }

            testcase.delete() // flush:true
        }
        render  status: success? "200" : "404"
    }
}
