package generator

class AjaxTagLib {
    static defaultEncodeAs = 'Raw'
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    def ajaxPanel = { attrs, body->
        def url = attrs.url
        def id = attrs.id

        out << """
                <div class="ajaxPanel" url="${url}" id="${id}">
                ${body()}
                </div>
            """

    }
}
