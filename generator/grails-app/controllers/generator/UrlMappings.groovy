package generator

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/testCase/$action?/$id?"(controller: "testcase")
        "/testcase/$action?/$id?"(controller: "testcase")

        "/"(controller: "project", action:"index" )
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
