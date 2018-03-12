package generator

import cn.wwt.appgen.HttpMethod
import cn.wwt.appgen.model.JavaType

class BootStrap {

    public static final String ARRAY = "Array"
    public static final String OBJECT = "Object"
    public static final String INTEGER = "Integer"
    public static final String STRING = "String"
    public static final String PASSWORD = "Password"
    public static final String LONG = "Long"
    public static final String FLOAT = "Float"
    public static final String DOUBLE = "Double"
    public static final String BIG_DECIMAL = "BigDecimal"
    public static final String DATE = "Date"
    public static final String TIME = "Time"
    public static final String DATE_TIME = "DateTime"
    public static final String FILE = "File"
    public static final String IMAGE = "Image"
    public static final String TAKE_PIC = "TakePic"
    public static final String PICK_PIC = "PickPic"
    public static final String VIDEO = "Video"
    public static final String TAKE_VIDEO = "TakeVideo"
    public static final String LOCATION = "Location"

    def init = { servletContext ->
        JavaType.findByName(INTEGER) ?: new JavaType(name: INTEGER).save()
        JavaType.findByName(STRING) ?: new JavaType(name: STRING).save()
        JavaType.findByName(PASSWORD) ?: new JavaType(name: PASSWORD).save()
        JavaType.findByName(LONG) ?: new JavaType(name: LONG).save()
        JavaType.findByName(FLOAT) ?: new JavaType(name: FLOAT).save()
        JavaType.findByName(DOUBLE) ?: new JavaType(name: DOUBLE).save()

        JavaType.findByName(BIG_DECIMAL) ?: new JavaType(name: BIG_DECIMAL).save()
        JavaType.findByName(DATE) ?: new JavaType(name: DATE).save()
        JavaType.findByName(TIME) ?: new JavaType(name: TIME).save()
        JavaType.findByName(DATE_TIME) ?: new JavaType(name: DATE_TIME).save()

        JavaType.findByName(FILE) ?: new JavaType(name: FILE).save()
        JavaType.findByName(IMAGE) ?: new JavaType(name: IMAGE).save()
        JavaType.findByName(TAKE_PIC) ?: new JavaType(name: TAKE_PIC).save()
        JavaType.findByName(PICK_PIC) ?: new JavaType(name: PICK_PIC).save()
        JavaType.findByName(VIDEO) ?: new JavaType(name: VIDEO).save()
        JavaType.findByName(TAKE_VIDEO) ?: new JavaType(name: TAKE_VIDEO).save()
        JavaType.findByName(LOCATION) ?: new JavaType(name: LOCATION).save()

        JavaType.findByName(ARRAY) ?: new JavaType(name: ARRAY).save()
        JavaType.findByName(OBJECT) ?: new JavaType(name: OBJECT).save()

        HttpMethod.findByName("get") ?: new HttpMethod(name: "get").save()
        HttpMethod.findByName("post") ?: new HttpMethod(name: "post").save()
        HttpMethod.findByName("put") ?: new HttpMethod(name: "put").save()
        HttpMethod.findByName("delete") ?: new HttpMethod(name: "delete").save()
    }
    def destroy = {
    }
}
