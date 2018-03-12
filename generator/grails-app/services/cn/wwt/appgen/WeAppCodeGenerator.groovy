package cn.wwt.appgen

import cn.wwt.utils.FileUtils
import cn.wwt.utils.StringUtils
import generator.BootStrap
import grails.transaction.Transactional

/**
 * Created by davidchen on 12/04/2017.
 */
@Transactional
class WeAppCodeGenerator {
    public static final appjsonTmpl = """
    {
  "pages": [
   \${pages}
  ],
  "tabBar": {

    "selectedColor":"#0000bb",
    "borderStyle":"white",
    "list": [
      {
        "pagePath": "pages/index",
        "text": "页面列表",
        "iconPath": "images/logo_i.png",
        "selectedIconPath": "images/logo.png"
      },
      {
        "pagePath": "pages/logs/logs",
        "text": "关于",
        "iconPath": "images/qs.png",
        "selectedIconPath": "images/qs.png"
      }
    ]
  },
  "window": {
    "navigationBarTextStyle": "black",
    "navigationBarTitleText": "\${title}",
    "navigationBarBackgroundColor": "#f8f8f8",
    "backgroundColor": "#f8f8f8"
  },
  "networkTimeout": {
    "request": 10000,
    "connectSocket": 10000,
    "uploadFile": 10000,
    "downloadFile": 10000
  },
  "debug": true
}
"""
    public static final listPagesItemTmpl ="""
      <block>
        <view class="kind-list__item">
          <view class="weui-flex kind-list__item-hd">
             <navigator url="\${pagePath}">
                <button class="weui-btn" type="primary">\${pagePath}</button>
            </navigator>
          </view>
        </view>
      </block>

"""
public static final inputWxmlTmpl = """\
<view class="page">
  <view class="weui-cells">
\${rows}
  </view>

  <view class="page__bd page__bd_spacing">
    <button class="weui-btn" type="primary" bindtap="accessServer">提交</button>
  </view>
</view>
"""

    public static final indexWxmlTmpl = """\
<view class="page">
  <view class="page__bd page__bd_spacing">
    <view class="kind-list">
        \${rows}
    </view>
  </view>
</view>
"""


    public static final pageJs = """\
var util = require("../../utils/util.js");
var today = util.formatDate(new Date());
var inputContent = {\${input}};
Page({

    data: {
        \${data}
    },

    \${methods}
});

"""

    public static final indexJs = """\
var util = require("../utils/util.js");
var today = util.formatDate(new Date());
var inputContent = {\${input}};
Page({

    data: {
        \${data}
    },

    \${methods}
});

"""

    public static final pageJson = """\
{}

"""

    public static final pageWxss = """\
.input{
    background-color: #eeeeee
}
"""
    public static final inputPageItemTmpl = """
            \${field}
"""


    public static final outputWxmlTmpl = """\
<view class="page">
  <view class="page__bd">
\${rows}
    </view>
</view>
"""

    public static final appJs ="""
var pagesData = [];
App({
    onLaunch: function () {
        console.log('App onLaunch')
        //调用API从本地缓存中获取数据
        var logs = wx.getStorageSync('logs') || []
        logs.unshift(Date.now())
        wx.setStorageSync('logs', logs)
    },
    onShow: function () {
        console.log('App Show')
    },
    onHide: function () {
        console.log('App Hide')
    },
    globalData: {
        hasLogin: false
    },

    getPageData:function(pageName){
        return pagesData[pageName];
    },

    setPageData:function(pageName,data){
        pagesData[pageName] = data
    },
    getUserInfo:function(cb){
        var that = this;
        if(this.globalData.userInfo){
            typeof cb == "function" && cb(this.globalData.userInfo)
        }else{
            //调用登录接口
            wx.login({
                success: function () {
                    wx.getUserInfo({
                        success: function (res) {
                            that.globalData.userInfo = res.userInfo;
                            typeof cb == "function" && cb(that.globalData.userInfo)
                        }
                    })
                }
            });
        }
    },
    globalData:{
        userInfo:null
    },
    url:"\${urlBase}"
});
"""
    public static final genCode(Project project, int version, String root) {

        String tmplWeAppDir = root + "/tmpl/weapp"
        String targetDir = StringUtils.removeTrailing(project.sourceDir, "/")
        def targetWeAppRoot = targetDir + "/src/app/weapp"
        String appDir = targetWeAppRoot + "/src"
        String pagesDir = appDir + "/pages"

        String urlBase = project.urlBase

        FileUtils.copyDir(tmplWeAppDir, appDir)

        List<ServerInterface> interfaces = ServerInterface.findAllByProjectAndApiVersion(project, version)
        List<String> allPages = ["pages/index"]
        List<String> inputPages = []
        interfaces.each { iface ->

            List<InputParam> inputParams = InputParam.findAllByServerInterface(iface)
            List<OutputParam> outputParams = OutputParam.findAllByServerInterface(iface)

            genInputWxml(pagesDir, iface.name, inputParams)
            FileUtils.makeFile("${pagesDir}/${iface.name}", "input.json", pageJson)
            FileUtils.makeFile("${pagesDir}/${iface.name}", "input.wxss", pageWxss)
            genInputJs(pagesDir,urlBase, iface, inputParams)
            String inputPageName ="pages/${iface.name}/input"
            allPages.add(inputPageName)
            inputPages.add("${iface.name}/input")

            String outputPageName = "pages/${iface.name}/output"
            allPages.add(outputPageName)

            genOutputWxml(pagesDir, iface.name, outputParams)
            FileUtils.makeFile("${pagesDir}/${iface.name}", "output.js", StringUtils.bind(outputJsTmpl,[inputPageName:"${iface.name}/input"]))
            FileUtils.makeFile("${pagesDir}/${iface.name}", "output.json", pageJson)
            FileUtils.makeFile("${pagesDir}/${iface.name}", "output.wxss", pageWxss)

        }

        //gen app.json
        def pageList =""
        for(int i =0 ;i < allPages.size();i++){
            pageList += "\"" + allPages[i] +"\""
            pageList +=  (i == allPages.size()-1) ? "" : ","
        }

        FileUtils.makeFile(appDir, "app.json", StringUtils.bind(WeAppCodeGenerator.appjsonTmpl,[pages: pageList, title:project.title]))
        FileUtils.makeFile(appDir, "app.js", StringUtils.bind(WeAppCodeGenerator.appJs,[urlBase: StringUtils.removeTrailing(project.urlBase,"/")]))

        def rows = ""
        inputPages.each {
            rows += "\n"+StringUtils.bind(listPagesItemTmpl,[pagePath: it])
        }
        FileUtils.makeFile(pagesDir, "index.wxml", StringUtils.bind(indexWxmlTmpl,[rows:rows]))
        FileUtils.makeFile(pagesDir, "index.js", StringUtils.bind(indexJs,[data:"",methods: "",input:""]))
        FileUtils.makeFile(pagesDir, "index.json", pageJson)
        FileUtils.makeFile(pagesDir, "index.wxss", pageWxss)
    }

    public static final outputJsTmpl = """
var util = require('../../utils/util.js')
Page({
  onLoad: function () {
    var data = getApp().getPageData("\${inputPageName}");
    this.setData(data);
  }
})
"""

    public static final accessServerTmpl = """
    bindChange: function(e) {
        var newData = this.data;
        newData[e.currentTarget.id] = e.detail.value;
        this.setData(newData);
        inputContent[e.currentTarget.id] = e.detail.value
    },

     accessServer:function(){
        var pageThis = this;
        var app = getApp();
        wx.request({
          url: app.url+'\${url}',
          data: inputContent,
          method: "\${method}", // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: {
            "content-type":"application/x-www-form-urlencoded"
          }, // 设置请求的 header
          success: function(res){
                var app = getApp();
                app.setPageData(\${inputPageName},res.data);
                wx.navigateTo({url: \${resultPagePath} });
          },
          fail: function(res) {
                wx.showToast({
                      title: '出错:'+res,
                      icon: 'fail',
                      duration: 2000
                    });
          },
          complete: function(res) {
            // complete
          }
        })
    }
"""
    public static final String genInputJs(String pagesDir, String urlBase, ServerInterface serverInterface, List<InputParam> inputParams) {
        def data = ""
        def inputContent =""
        inputParams.each {
            switch (it.type.name) {
                case BootStrap.INTEGER:
                case BootStrap.LONG:
                case BootStrap.FLOAT:
                case BootStrap.DOUBLE:
                case BootStrap.BIG_DECIMAL:
                case BootStrap.STRING:
                case BootStrap.PASSWORD:
                    data += "${it.name}:\"\","
                    break
                case BootStrap.DATE:
                    data += "${it.name}:today,"
                    inputContent += "${it.name}:today,"
                    break
                case BootStrap.TIME:
                    data += "${it.name}:\"8:00\","
                    inputContent += "${it.name}:\"8:00\","
                    break
                case BootStrap.DATE_TIME:
                    data += "${it.name}:\"8:00\","
                    inputContent += "${it.name}:\"8:00\","
                    break
//                        BootStrap.FILE
//                        BootStrap.IMAGE
//                        BootStrap.TAKE_PIC
//                        BootStrap.PICK_PIC
//                        BootStrap.VIDEO
//                        BootStrap.TAKE_VIDEO
//                        BootStrap.LOCATION
//                        BootStrap.ARRAY
//                        BootStrap.OBJECT
            }
        }
        final def project = serverInterface.project
        def url = "/phoneApi/v"+serverInterface.apiVersion+"/" +  serverInterface.name
        String method = (serverInterface.method.name as String).toUpperCase()
        def methods = StringUtils.bind(accessServerTmpl,[url:url,method:method,
                                                         inputPageName:"\"${serverInterface.name}/input\"",
                                                         resultPagePath:"\"output\""])
        def content = StringUtils.bind(pageJs, [data: data,methods:methods,input: inputContent])
        FileUtils.makeFile("${pagesDir}/${serverInterface.name}", "input.js", content)
        return "${pagesDir}/${serverInterface.name}/input"
    }

    public static final inputRowTmpl = """
   <view class="weui-cell">
      <view class="weui-cell__bd">\${name}</view>
      <input id="\${name}" bindchange="bindChange" class="weui-cell__bd input" type="\${type}" value="{{\${name}}}" \${isPassword}></input>
   </view>
"""

    public static final inputDateTmpl = """
    <view class="weui-cell">
      <view class="weui-cell__bd">\${name}(点击修改):</view>
      <picker id="\${name}" mode="\${mode}"  bindchange="bindChange">
        <view class="picker"> {{\${name}}} </view>
      </picker>
    </view>
"""
    public static final String genInputWxml(String pagesDir, String interfaceName, List<InputParam> inputParams) {
        def rows = ""

        inputParams.each {
            def field = ""
            switch (it.type.name) {
                case BootStrap.INTEGER:
                case BootStrap.LONG:
                case BootStrap.FLOAT:
                case BootStrap.DOUBLE:
                case BootStrap.BIG_DECIMAL:
                    field += StringUtils.bind(inputRowTmpl,[name: it.name,type:"digit",isPassword:""])
                    break
                case BootStrap.STRING:
                    field += StringUtils.bind(inputRowTmpl,[name: it.name,type:"text",isPassword:""])
                    break
                case BootStrap.PASSWORD:
                    field += StringUtils.bind(inputRowTmpl,[name: it.name,type:"text",isPassword:"password=\"true\""])
                    break
                case BootStrap.DATE:
                    field += StringUtils.bind(inputDateTmpl,[name: it.name,mode:"date"])
                    break
                case BootStrap.TIME:
                    field += StringUtils.bind(inputDateTmpl,[name: it.name,mode:"time"])
                    break
                case BootStrap.DATE_TIME:
                    field += StringUtils.bind(inputDateTmpl,[name: it.name,mode:"date"])
                    break
//                        BootStrap.FILE
//                        BootStrap.IMAGE
//                        BootStrap.TAKE_PIC
//                        BootStrap.PICK_PIC
//                        BootStrap.VIDEO
//                        BootStrap.TAKE_VIDEO
//                        BootStrap.LOCATION
//                        BootStrap.ARRAY
//                        BootStrap.OBJECT
            }
            rows += StringUtils.bind(inputPageItemTmpl,[field: field])
        }

        def content = StringUtils.bind(inputWxmlTmpl, [rows: rows])
        FileUtils.makeFile("${pagesDir}/${interfaceName}", "input.wxml", content)
        return "${pagesDir}/${interfaceName}/input"
    }

    public static final outputPageItemTmpl = """
    <view class="kind-list__item">
    <view class="weui-flex kind-list__item-hd">
        <view class="weui-flex__item">\${name}</view>
         <input class="kind-list__input" type="digit" value="{{\${value}}}" />
    </view>
        </view>
    """

    public static final arrayOrObjectSectionTmpl = """
  <view class="weui-cells weui-cells_after-title">
    <view class="weui-cells__title">\${objOrArrayName}:</view>
        \${arrayOrObject}
    </view>
    """
    public static final outputPageArrayTmpl = """
    <view  wx:for="{{\${array}}}" class="weui-cells weui-cells_after-title">
        \${fields}
     </view>
    """

    public static final outputPageArrayFieldTmpl = """
        <view class="weui-cell">
            <view class="weui-cell__bd">\${name}:</view>
            <view class="weui-cell__ft">
                  <input class="kind-list__input" type="digit" value="{{item.\${name}}}" />
            </view>
        </view>
    """

    public static final outputPageObjectFieldTmpl = """
        <view class="weui-cell">
            <view class="weui-cell__bd">\${fname}:</view>
            <view class="weui-cell__ft">
                  <input class="kind-list__input" type="digit" value="{{\${pname}.\${fname}}}" />
            </view>
        </view>
    """
    public static final String genOutputWxml(String pagesDir, String interfaceName, List<OutputParam> outputParams) {
        def rows = ""

        outputParams.each { param->
            switch (param.type.name) {
                case BootStrap.INTEGER:
                case BootStrap.LONG:
                case BootStrap.FLOAT:
                case BootStrap.DOUBLE:
                case BootStrap.BIG_DECIMAL:
                case BootStrap.STRING:
                case BootStrap.PASSWORD:
                case BootStrap.DATE:
                    rows += StringUtils.bind(outputPageItemTmpl,[name: param.name, value: param.name ])
                    break

//                        BootStrap.FILE
//                        BootStrap.IMAGE
//                        BootStrap.TAKE_PIC
//                        BootStrap.PICK_PIC
//                        BootStrap.VIDEO
//                        BootStrap.TAKE_VIDEO
//                        BootStrap.LOCATION
                case     BootStrap.ARRAY:

                    def fields = ""
                    param.fields.each { f->
                        fields += StringUtils.bind(outputPageArrayFieldTmpl,[name: f.name ])
                    }
                    def array = StringUtils.bind(outputPageArrayTmpl,[array: param.name,fields:fields])

                    rows += StringUtils.bind(arrayOrObjectSectionTmpl,[objOrArrayName: param.name,arrayOrObject:array])
                    break;

                case  BootStrap.OBJECT:
                    def fields = ""
                    param.fields.each { f->
                        fields += StringUtils.bind(outputPageObjectFieldTmpl,[fname: f.name, pname: param.name])
                    }
                    rows += StringUtils.bind(arrayOrObjectSectionTmpl,[objOrArrayName: param.name,arrayOrObject:fields])

                    break;
            }
        }

        def content = StringUtils.bind(outputWxmlTmpl, [rows: rows])
        FileUtils.makeFile("${pagesDir}/${interfaceName}", "output.wxml", content)
        return "${pagesDir}/${interfaceName}/output"
    }

    public static final normalOutputField ="""
             <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="\${name}:" />

                    <TextView
                        android:id="@+id/\${name}_result"
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="2" />
                </LinearLayout>
"""


}
