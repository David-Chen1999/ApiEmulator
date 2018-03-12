package cn.wwt.appgen.model

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='name')
class JavaType {
    String name
    String toIOSName(){
        switch (name){
            case "Integer":	return "Int"
            case "String":      return "String"
            case "Password":      return "String"
            case "Long":      return "Int"
            case "Float":      return "Float"
            case "Double":      return "Double"
            case "BigDecimal":      return "Decimal"
            case "Date":      return "NSDate"
            case "Time":      return "NSDate"
            case "DateTime":      return "NSDate"
            case "File":      return "File"
            case "Image":      return "Image"
            case "TakePic":      return "TakePic"
            case "PickPic":      return "PickPic"
            case "Video":      return "Video"
            case "TakeVideo":      return "TakeVideo"
            case "Location":      return "Location"
            case "Array":      return "Array"
            case "Object":      return "NSObject"
        }
    }
    static constraints = {
        name unique: true
    }

    @Override
    String toString() {
        return name + ":" + id
    }
}
