package cn.wwt.utils

import groovy.text.SimpleTemplateEngine

/**
 * Created by davidchen on 07/04/2017.
 */
class StringUtils {
    public static String firstCharToUpperCase(String str){
        String firstChar = str.charAt(0)
        return firstChar.toUpperCase() + str.substring(1)
    }

    public static String removeTrailing(String str,String trailing){
        while(str[str.length()-1] == trailing){
            str = str.substring(0,str.length()-1)
        }
        return str
    }
    public static String removeLeading(String str,String leading){
        while(str[0] == leading){
            str = str.substring(1,str.length())
        }
        return str
    }

    public static String bind(String template,Map binding){
        SimpleTemplateEngine engine = new SimpleTemplateEngine()
        return engine.createTemplate(template).make(binding).toString()
    }
}
