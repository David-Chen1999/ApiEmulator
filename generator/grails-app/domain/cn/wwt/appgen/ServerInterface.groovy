package cn.wwt.appgen

class ServerInterface {
    Project project
    String name
    Integer apiVersion
    String description
//    String relativeUrl
    HttpMethod method

    static hasMany = [inputParams: InputParam, outputParams:OutputParam, testCases: TestCase]
    static mapping = {
        inputParams cascade:"none", lazy: false
        outputParams cascade:"none", lazy: false
        testCases cascade:"none", lazy: false
    }

    static constraints = {
        project nullable: true
        apiVersion nullable: true
    }
}
