package cn.wwt.appgen

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='name')
class HttpMethod {
    String name
    static constraints = {
        name unique: true
    }

    @Override
    String toString() {
        return name + ":" + id
    }
}
