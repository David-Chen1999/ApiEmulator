package generator

class QSDisplayTagLib {
    static defaultEncodeAs = 'Raw'
    def showBean =  { attrs, body ->
        def tableStart = "<table width='80%'><tbody>"
        out << tableStart
        def properties = attrs.properties
        def bean =resolveBean(attrs.bean)
        for(int i = 0; i < properties.size();i++){
            def e = properties[i]
            def rowClass = i % 2 > 0 ? "odd" : "even"
            if(e instanceof String){
                def td = "<tr class='${rowClass}'><td>${e}</td><td>${bean.getAt(e)}</td></tr>"
                out << td
            }
        }
        out<< "</tbody></table>"
    }

    private Object resolveBean(beanAttribute) {
        resolvePageScopeVariable(beanAttribute) ?: beanAttribute
    }

    private resolvePageScopeVariable(attributeName) {
        // Tomcat throws NPE if you query pageScope for null/empty values
        attributeName?.toString() ? pageScope.variables[attributeName] : null
    }

    def dateField = { attrs, body ->
        int yearRange = 100
        if(attrs.yearRange){
            yearRange = attrs.yearRange as int
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR)
        int startYear = currentYear - yearRange/2

        def bean = resolveBean(attrs.bean)
        def label = attrs.property

        def val = bean.getAt(label)
        def model = [label: label, yearRange: yearRange, startYear:startYear]
        if(val && val instanceof Date){
            def date = (Date)val
            def calendar = Calendar.getInstance()
            calendar.setTime(date)
            def year = calendar.get(Calendar.YEAR)
            def month = calendar.get(Calendar.MONTH)
            def day = calendar.get(Calendar.DAY_OF_MONTH)
            model.putAll([year: year, month: month, day : day])
        }
       out << render(model: model,template: "/tmpls/zhdate")
    }
}
