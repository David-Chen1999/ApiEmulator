package cn.wwt.appgen

class OutputParamValue {
    TestCase testCase
    OutputParam type
    String value

    static belongsTo = [testCase: TestCase]
    static constraints = {
    }

    static mapping = {
        value column: 'value', sqlType: "Text"
    }
}
