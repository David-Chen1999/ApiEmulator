package cn.wwt.appgen

class InputParamValue {
    TestCase testCase
    InputParam type
    String value

    static belongsTo = [testCase: TestCase]
    static constraints = {
    }
}
