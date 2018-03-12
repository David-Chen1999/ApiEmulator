package cn.wwt.appgen

class TestCase {

    ServerInterface serverInterface
    String name
    String responseCode
    String responseMsg
    Boolean success
    String description

    List<InputParamValue> inputs
    List<OutputParamValue> outputs

    static belongsTo = [serverInterface: ServerInterface]

    static hasMany = [inputs: InputParamValue, outputs:OutputParamValue]

    static constraints = {
        serverInterface nullable: true
    }
}
