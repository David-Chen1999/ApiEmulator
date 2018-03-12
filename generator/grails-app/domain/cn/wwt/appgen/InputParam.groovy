package cn.wwt.appgen

import cn.wwt.appgen.model.JavaType
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = 'id')
class InputParam {
    ServerInterface serverInterface
    String name
    JavaType type
    String description
    List fields
    static hasMany = [fields : InputParam] //if type is object, fields are attributes of this object

    static constraints = {
        type nullable: true
        serverInterface nullable: true
    }
}
