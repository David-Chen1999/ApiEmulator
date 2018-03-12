package cn.wwt.appgen

import cn.wwt.appgen.model.JavaType
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = 'id')
class OutputParam {
    ServerInterface serverInterface
    String name
    JavaType type
    String description
    List<OutputParam> fields
    static hasMany = [fields : OutputParam] //if type is object, fields are attributes of this object

    static constraints = {
        serverInterface nullable: true
        fields nullable: true
        description nullable: true

    }
    static mapping = {
        fields lazy: false
    }


}
