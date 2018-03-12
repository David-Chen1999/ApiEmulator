package cn.wwt.appgen

import cn.wwt.utils.FileUtils
import cn.wwt.utils.StringUtils
import generator.BootStrap
import grails.transaction.Transactional
/**
 * Created by davidchen on 12/04/2017.
 */
@Transactional
class AndroidCodeGenerator {
    public static final outputRowLayout = """
    <TableRow>
         <TextView android:width="0dp" android:layout_weight="1" android:text=\"\${name}\"/>
         <TextView android:width="0dp" android:layout_weight="1" android:id=\"@+id/\${name}_result\"/>
    </TableRow>"""
    public static final manifestTmpl="""<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="\${packageName}">
      
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
      
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
      
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
      
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
      
    <uses-permission android:name="android.permission.INTERNET" />
      
    <uses-permission android:name="android.permission.WAKE_LOCK" />

    <!-- To auto-complete the email text field in the Login form with the user's emails -->
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.READ_PROFILE" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.FLASHLIGHT" />

    <application
        android:name="cn.qs.android.App"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.Design.Light.NoActionBar">
        <activity
            android:name=".activity.MainActivity"
            android:windowSoftInputMode="stateUnchanged">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        \${activities}
        <receiver android:name="cn.qs.android.receiver.InstallReceiver">
            <intent-filter>
                <action android:name="android.intent.action.DOWNLOAD_COMPLETE" />
            </intent-filter>
        </receiver>

    </application>

</manifest>
"""
    public static final inputLayout = """\
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white"
    android:orientation="vertical">

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="20dp"
        android:layout_gravity="center_horizontal"
        android:text="输入参数" />

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
         >

            <TableLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">
            \${rows}
            </TableLayout>
        </ScrollView>
    </FrameLayout>

    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:onClick="submit"
        android:text="提交" />
</LinearLayout>
"""
    public static final outputLayout = """\
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white"
    android:orientation="vertical">

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="20dp"
        android:layout_gravity="center_horizontal"
        android:text="输出结果" />

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
         >

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">
            \${rows}
            </LinearLayout>
        </ScrollView>
    </FrameLayout>

    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:onClick="close"
        android:text="关闭" />
</LinearLayout>
"""
    public static final outputItemLayout = """\
<?xml version="1.0" encoding="utf-8"?>
<TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginLeft="2dp"
    android:layout_marginRight="2dp"
    android:gravity="center_vertical"
    android:orientation="horizontal">

    \${rows}
</TableLayout>
"""
    public static final strings= """
<resources>
    <string name="app_name">\${appName}</string>
</resources>
"""
    public static final mainActivity ="""package \${packageName}.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.qs.android.base.BaseActivity;
import \${packageName}.R;

public class MainActivity extends BaseActivity {



    @Override
    protected int getContentViewId() {
        return R.layout.main_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }


    public void submit(View view) {
        Object tag = view.getTag();
        String className = (String)tag;
        try {

            Class<?> aClass = Class.forName(className);
            startActivity(new Intent(this, aClass));
        }catch (Exception e){
            Toast.makeText(this, "没有找到Activity：" + className, Toast.LENGTH_SHORT).show();
        }

    }
}

"""
    public static final mainLayout = """<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white"
    android:orientation="vertical">

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="20dp"
        android:layout_gravity="center_horizontal"
        android:text="输入Activity列表" />

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <TableLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">
                \${buttons}

            </TableLayout>
        </ScrollView>
    </FrameLayout>
</LinearLayout>

"""
    public static final buttonLayout = """
    <TableRow>
    <Button android:id="@+id/btn\${activityName}" android:textAllCaps="false"
    android:tag="\${activityName}" android:text="\${activityName}" android:onClick="submit" android:layout_width="match_parent" android:layout_height="wrap_content"/>
    </TableRow>
"""
    public static final inputActivityClass = """
package \${packageName}.activity;

import android.os.Bundle;
import butterknife.BindView;
import cn.qs.android.base.BaseActivity;
import \${packageName}.R;
import \${packageName}.Constants;
import cn.qs.android.httpclient.ServerResponse;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.google.gson.reflect.TypeToken;
import cn.qs.android.httpclient.HttpClient;
import cn.qs.android.httpclient.callback.OnComplete;
import cn.qs.android.httpclient.callback.OnSuccess;
import cn.qs.android.httpclient.TURL;
import okhttp3.Headers;
import java.math.BigDecimal;
import cn.qs.android.utils.PromptUtil;
import java.util.List;
import java.io.Serializable;
import android.view.View;

public class \${activityName}Activity extends BaseActivity {

    \${bindView}

    @Override
    protected int getContentViewId() {
        return R.layout.\${inputLayoutName};
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    static final TURL \${interfaceName}TURL = new TURL(\${url},
        new TypeToken<\${interfaceName}Response>() {},
        new TypeToken<\${interfaceName}Response>() {});

    \${innerClass}
    public static class \${interfaceName}Response extends ServerResponse{
        \${responseClass}
    }
    \${onsubmit}
}

"""
    public static final appBuildGradle = """
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'me.tatarka:gradle-retrolambda:3.4.0'
        classpath 'com.jakewharton:butterknife-gradle-plugin:8.4.0'
    }
}

// Required because retrolambda is on maven central
repositories {
    mavenCentral()
}

apply plugin: 'com.android.application'
apply plugin: 'me.tatarka.retrolambda'
apply plugin: 'com.jakewharton.butterknife'

android {
    compileSdkVersion 24
    buildToolsVersion '25.0.0'
    defaultConfig {
        applicationId "\${packageName}"
        minSdkVersion 19
        targetSdkVersion 24
        versionCode 12
        versionName "2.2"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }


    // signingConfigs {
    //     release {
    //         storeFile project.rootProject.file('app/signing.jks')
    //         storePassword "qs2017"
    //         keyAlias "2wintech"
    //         keyPassword "qs2017"
    //     }
    // }
    buildTypes {
        release {
            // signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'),'proguard-rules.pro'
        }

        debug {
            // signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles 'proguard-rules.pro'
        }

    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })

    // MPAndroidChart
    compile 'com.android.support:appcompat-v7:24.2.1'
    compile 'com.squareup.okhttp3:okhttp:3.4.1'
//    compile 'com.squareup.okhttp3:mockwebserver:3.4.1'

    compile 'com.google.code.gson:gson:2.2.4'
    compile 'com.jakewharton:butterknife:8.4.0'
    compile 'org.greenrobot:eventbus:3.0.0'
    compile 'com.android.support:design:24.2.1'
//    compile 'com.github.lecho:hellocharts-library:1.5.8@aar'
    compile 'com.github.PhilJay:MPAndroidChart:v3.0.1'
    compile 'com.android.support:support-v4:24.2.1'
    testCompile 'junit:junit:4.12'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'

    compile 'cn.bingoogolapple:bga-qrcodecore:1.1.7@aar'
    compile 'cn.bingoogolapple:bga-zbar:1.1.7@aar'
    compile 'pub.devrel:easypermissions:0.1.7'

    compile 'io.reactivex:rxandroid:1.2.1'
    compile 'io.reactivex:rxjava:1.2.5'

    compile 'com.github.NaikSoftware:StompProtocolAndroid:1.3.1'
    //StompProtocolAndroid 依赖于webSocket的标准实现
    compile 'org.java-websocket:Java-WebSocket:1.3.4'

    //https://github.com/JZXiang/TimePickerDialog
    compile 'com.jzxiang.pickerview:TimePickerDialog:1.0.1'

    //https://github.com/nostra13/Android-Universal-Image-Loader
    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
}

"""

    public static final outputActivityClass = """
package \${packageName}.activity;
import android.widget.ListView;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import android.widget.TextView;
import cn.qs.android.base.BaseActivity;
import \${packageName}.R;
import \${packageName}.Constants;
\${importAdapter}
public class \${activityName}Activity extends BaseActivity {
    \${bindView}

    @Override
    protected int getContentViewId() {
        return R.layout.\${outputLayoutName};
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Object result = getIntent().getSerializableExtra("\${inputActivityName}_result");
        \${inputActivityName}Activity.\${responseClassName}Response response = (\${inputActivityName}Activity.\${responseClassName}Response) result;

        \${outputValues}
    }

    public void close(View view) {
        finish();
    }

}
"""
    public static final subRowInOutputLayout ="""
     <TableRow>
            <TextView android:text=\"\${name}:\" android:layout_width="0dp" android:layout_weight="1"/>
            <TextView android:id=\"@+id/\${name}_result\" android:layout_width="0dp" android:layout_weight="2"/></TableRow>
                            """
    public static final listAdapterClass = """
package \${packageName}.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import \${packageName}.R;
import \${packageName}.activity.\${inputActivityName};

public class \${adapterName}Adapter extends BaseAdapter {

    public List<\${itemClass}> mList;
    private LayoutInflater mInflater;

    public \${adapterName}Adapter(Context context, List<\${itemClass}> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int index) {
        return mList.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        \${adapterName}Adapter.ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.\${itemLayout}, parent, false);
            holder = new \${adapterName}Adapter.ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (\${adapterName}Adapter.ViewHolder)view.getTag();
        }

        \${itemClass} item = mList.get(position);

        \${itemValues}

        return view;
    }


    public final class ViewHolder {

    \${bindView}

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
"""
    public static final appConstantsFile = """
    package \${packageName};
    public class Constants{
        public static final String URL_BASE="\${projectUrlBase}";
    } 
"""
    public static final genCode(Project project, int version, String root) {

        String tmplAndroidDir = root + "/tmpl/android/tmplAndroid"
        String targetDir = StringUtils.removeTrailing(project.sourceDir, "/")
        def targetAndroidRoot = targetDir + "/src/app/android"
        String appDir = targetAndroidRoot + "/app"
        String manifest = appDir + "/src/main/AndroidManifest.xml"
        String resDir = appDir + "/src/main/res"
        String layoutDir = resDir + "/layout"
        String classDir = appDir + "/src/main/java"
        new File(layoutDir).mkdirs()
        String srcDirs = ["activity", "adapter", "fragment", "utils", "dto", "server"]
        String urlBase = project.urlBase

        FileUtils.copyDir(tmplAndroidDir, targetAndroidRoot)
        FileUtils.makeClass(classDir,project.packageName+".activity","MainActivity.java",StringUtils.bind(AndroidCodeGenerator.mainActivity,[packageName:project.packageName]))
        String constantUrlBase = StringUtils.removeTrailing(project.urlBase,"/") ;
        FileUtils.makeClass(classDir,project.packageName,"Constants.java",
                StringUtils.bind(AndroidCodeGenerator.appConstantsFile,[projectUrlBase:constantUrlBase,packageName:project.packageName]))

        FileUtils.makeFile(appDir,"build.gradle",StringUtils.bind(AndroidCodeGenerator.appBuildGradle,[packageName:project.packageName]))
        FileUtils.makeFile(resDir+"/values","strings.xml",StringUtils.bind(AndroidCodeGenerator.strings,[appName:project.name]))

        List<ServerInterface> interfaces = ServerInterface.findAllByProjectAndApiVersion(project, version)
        List<String> activities = []
        List<String> inputActivities = []
        interfaces.each { iface ->

            List<InputParam> inputParams = InputParam.findAllByServerInterface(iface)
            List<OutputParam> outputParams = OutputParam.findAllByServerInterface(iface)

            String inputLayoutName = genAndroidInputLayout(layoutDir, iface.name, inputParams)
            String outputLayoutName = genAndroidOutputLayout(layoutDir, iface.name, outputParams)
            String inputActivityName = StringUtils.firstCharToUpperCase(iface.name) + "Input"
            activities.add(inputActivityName)
            inputActivities.add(inputActivityName)
            genAndroidInputActivity(classDir, iface, inputActivityName, inputLayoutName, inputParams,outputParams, urlBase)
            String outputActivityName = StringUtils.firstCharToUpperCase(iface.name) + "Output"
            activities.add(outputActivityName)
             genAndroidOutputActivity(classDir, iface, inputActivityName,outputActivityName, outputLayoutName, outputParams)
        }

        def listActivities = ""
        activities.each {
            listActivities += "<activity android:name=\".activity.${it}Activity\" />"
        }
        new File(manifest).withPrintWriter("utf-8") { f->
            f.write(StringUtils.bind(AndroidCodeGenerator.manifestTmpl,[packageName:project.packageName, activities:listActivities]))
        }

        def buttons = ""
        inputActivities.each {
            def activityName = project.packageName + ".activity." + it + "Activity"
            buttons += StringUtils.bind(AndroidCodeGenerator.buttonLayout,[activityName: activityName])
        }
        FileUtils.makeFile(layoutDir, "main_layout.xml", StringUtils.bind(AndroidCodeGenerator.mainLayout,[buttons:buttons]))

//        genTestMainActivity()
    }

    public static final String genAndroidInputLayout(String layoutDir, String interfaceName, List<InputParam> inputParams) {
        def rows = ""

        inputParams.each {
            def row = "\n     <TableRow>\n      <TextView android:text=\"${it.name}\" android:layout_width=\"0dp\" android:layout_weight=\"1\"/>\n"
            switch (it.type.name) {
                case BootStrap.INTEGER:
                case BootStrap.LONG:
                case BootStrap.FLOAT:
                case BootStrap.DOUBLE:
                case BootStrap.BIG_DECIMAL:
                    row += "\n        <EditText android:inputType=\"number\" android:id=\"@+id/${it.name}_edit\" android:layout_width=\"0dp\" android:layout_weight=\"2\"/>"
                    break
                case BootStrap.STRING:
                    row += "\n        <EditText android:id=\"@+id/${it.name}_edit\" android:layout_width=\"0dp\" android:layout_weight=\"2\"/>"
                    break
                case BootStrap.PASSWORD:
                    row += "\n        <EditText android:inputType=\"textPassword\" android:id=\"@+id/${it.name}_edit\" android:layout_width=\"0dp\" android:layout_weight=\"2\"/>"
                    break
                case BootStrap.DATE:
                    row += "\n        <EditText android:inputType=\"date\" android:id=\"@+id/${it.name}_edit\" android:layout_width=\"0dp\" android:layout_weight=\"2\"/>"
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
            row += "\n           </TableRow>"
            rows += row
        }
        def binding = ["rows": rows]
        def layoutName = interfaceName.toLowerCase() + "_input_layout"

        String targetFile = layoutDir + "/" + layoutName + ".xml"

        def engine = new groovy.text.SimpleTemplateEngine()
        def template = engine.createTemplate(AndroidCodeGenerator.inputLayout).make(binding)
//        new File(targetFile).withPrintWriter("utf-8") { writer ->
//
//            writer.write(template.toString().getBytes("utf-8"))
//        }
        def stream = new FileOutputStream(new File(targetFile))
        stream.write(template.toString().getBytes("utf-8"))
        stream.close()
        return layoutName
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

    public static final String genAndroidOutputLayout(String layoutDir, String interfaceName, List<OutputParam> outputParams) {
        def rows = ""

        outputParams.each { param->
            def row = ""
            switch (param.type.name) {
                case BootStrap.INTEGER:
                case BootStrap.LONG:
                case BootStrap.FLOAT:
                case BootStrap.DOUBLE:
                case BootStrap.BIG_DECIMAL:
                case BootStrap.STRING:
                case BootStrap.PASSWORD:
                case BootStrap.DATE:
                    row += StringUtils.bind(normalOutputField,[name:param.name])
                    break
//                        BootStrap.FILE
//                case     BootStrap.IMAGE:

//                        BootStrap.TAKE_PIC
//                        BootStrap.PICK_PIC
//                        BootStrap.VIDEO
//                        BootStrap.TAKE_VIDEO
//                        BootStrap.LOCATION
                case BootStrap.ARRAY:

                    row += """\n     <ListView android:layout_width="match_parent"
                                    android:layout_height="200dp" android:id="@+id/${param.name}_list">
                                </ListView>"""
                    def subRows = ""
                    param.fields.each { f ->
                        subRows += StringUtils.bind(AndroidCodeGenerator.outputRowLayout,[name:f.name])
                    }
                    def subBinding = ["rows": subRows]
                    def itemLayoutName = param.name.toLowerCase() + "_item_layout"
//                    String targetItemFile = layoutDir + "/" + itemLayoutName + ".xml"
//                    def engine = new groovy.text.SimpleTemplateEngine()
//                    def template = engine.createTemplate(AndroidCodeGenerator.outputItemLayout).make(subBinding)
//                    new File(targetItemFile).withPrintWriter { writer ->
//                        writer.write(template.toString())
//                    }

                    FileUtils.makeFile(layoutDir,itemLayoutName + ".xml",StringUtils.bind(outputItemLayout,subBinding))
                    break
                case BootStrap.OBJECT:
                    row += """
    <TableLayout android:layout_width="match_parent"
           android:layout_height="wrap_content" android:id="@+id/${param.name}_container">
          <TextView android:text="${param.name}:" android:layout_width="match_parent" />\n
          """
                    def subRows = ""
                    param.fields.each { f ->
                        subRows += StringUtils.bind(subRowInOutputLayout,[name:param.name +"_" + f.name])
                    }
                    row += subRows
                    row += "\n    </TableLayout>"
            }
            rows += row
        }
        def binding = ["rows": rows]
        def layoutName = interfaceName.toLowerCase() + "_output_layout"

        String targetFile = layoutDir + "/" + layoutName + ".xml"

        def engine = new groovy.text.SimpleTemplateEngine()
        def template = engine.createTemplate(AndroidCodeGenerator.outputLayout).make(binding)
//        new File(targetFile).withPrintWriter("utf-8") { writer ->
//            writer.write(template.toString().getBytes("utf-8"))
//        }
        new FileOutputStream(new File(targetFile)).write(template.toString().getBytes("utf-8"))
        return layoutName
    }

    public static final genAndroidInputActivity(String classDir, ServerInterface serverInterface, String activityName, String inputLayoutName, List<InputParam> inputParams, List<OutputParam> outputParams,
                                                String urlBase) {
        final def project = serverInterface.project
        def url =  "Constants.URL_BASE +\"/phoneApi/v"+serverInterface.apiVersion+"/" +  serverInterface.name+"\""
        def responseClassName = StringUtils.firstCharToUpperCase(serverInterface.name)
        def binding = [packageName: project.packageName, interfaceName: responseClassName, url: url,activityName:activityName,inputLayoutName:inputLayoutName]
        //gen bindview variables
        genInputActivityBindView(inputParams, binding)
        genInputActivityResponseInnerClass(outputParams, binding)
        String outputActivityName = StringUtils.firstCharToUpperCase(serverInterface.name) + "Output"
        def onsubmit = "public void submit(View view){"
//            onsubmit +="" //TODO validate
            onsubmit += "       HttpClient.request(${responseClassName}TURL)"
            inputParams.each {
                if("get".equalsIgnoreCase(serverInterface.method.name)){
                    onsubmit += "\n           .addUrlParam(\"${it.name}\",${it.name}Edit.getText().toString().trim())"
                }else{
                    onsubmit += "\n           .addBodyParam(\"${it.name}\",${it.name}Edit.getText().toString().trim())"
                }

            }
            onsubmit +=            """
                 .setOnSuccess(new OnSuccess<${responseClassName}Response>(){
                        @Override
                        public void onSuccess(int status, Headers headers, ${responseClassName}Response body) {
                                  Intent intent = new Intent(${activityName}Activity.this, ${outputActivityName}Activity.class);
                                  intent.putExtra("${activityName}_result", body);
                                  startActivity(intent);
                        }
                 })
                 .setOnFailure((int status, Headers headers, Object body) -> {
                    dismissLoading();
                    PromptUtil.showToast(${activityName}Activity.this, body.toString());
                })
                .setOnComplete((int compCode) -> {
                    dismissLoading();
                    if (compCode == OnComplete.TIMEOUT) {
                        PromptUtil.showToast(${activityName}Activity.this, "请求超时！");
                    } else if (compCode == OnComplete.NETWORK_ERROR) {
                        PromptUtil.showToast(${activityName}Activity.this, "网络错误！");
                    } else if (compCode == OnComplete.EXCEPTION) {
                        PromptUtil.showToast(${activityName}Activity.this, "网络错误！");
                    }

                })
                 .${serverInterface.method.name}();
             """
        onsubmit += "\n}"
        binding.put("onsubmit",onsubmit)

        String result = StringUtils.bind(inputActivityClass,binding)
        FileUtils.makeClass(classDir,project.packageName+".activity","${activityName}Activity.java",result)

    }

    public static final void genInputActivityResponseInnerClass(List<OutputParam> outputParams, LinkedHashMap<String, String> binding) {
        def innerClass = ""
        //gen response class
        def responseClass = ""
        outputParams.each {
            switch (it.type.name) {
                case BootStrap.INTEGER:
                    responseClass += "\n        public Integer ${it.name};\n"
                    break
                case BootStrap.LONG:
                    responseClass += "\n        public Long ${it.name};\n"
                    break
                case BootStrap.FLOAT:
                    responseClass += "\n        public Float ${it.name};\n"
                    break
                case BootStrap.DOUBLE:
                    responseClass += "\n        public Double ${it.name};\n"
                    break
                case BootStrap.BIG_DECIMAL:
                    responseClass += "\n        public BigDecimal ${it.name};\n"
                    break
                case BootStrap.PASSWORD:
                case BootStrap.STRING:
                    responseClass += "\n        public String ${it.name};\n"
                    break
                case BootStrap.DATE:
                    responseClass += "\n        public Date ${it.name};\n"
                    break
                case BootStrap.ARRAY:
                    def innerClassName = StringUtils.firstCharToUpperCase(it.name) + "Field"
                    responseClass += "\n        public List<${innerClassName}> ${it.name};\n"
                    innerClass += "public static class ${innerClassName} implements Serializable{"
                    it.fields.each {
                        switch (it.type.name) {
                            case BootStrap.INTEGER:
                                innerClass += "\n        public Integer ${it.name};\n"
                                break
                            case BootStrap.LONG:
                                innerClass += "\n        public Long ${it.name};\n"
                                break
                            case BootStrap.FLOAT:
                                innerClass += "\n        public Float ${it.name};\n"
                                break
                            case BootStrap.DOUBLE:
                                innerClass += "\n        public Double ${it.name};\n"
                                break
                            case BootStrap.BIG_DECIMAL:
                                innerClass += "\n        public BigDecimal ${it.name};\n"
                                break
                            case BootStrap.PASSWORD:
                            case BootStrap.STRING:
                                innerClass += "\n        public String ${it.name};\n"
                                break
                            case BootStrap.DATE:
                                innerClass += "\n        public Date ${it.name};\n"
                                break
                        }
                    }
                    innerClass += "\n       }"
                    break
                case BootStrap.OBJECT:
                    def innerClassName = StringUtils.firstCharToUpperCase(it.name) + "Field"
                    responseClass += "\n        ${innerClassName} ${it.name};\n"
                    innerClass += "public static class ${innerClassName}  implements Serializable{"
                    it.fields.each {
                        switch (it.type.name) {
                            case BootStrap.INTEGER:
                                innerClass += "\n        Integer ${it.name};\n"
                                break
                            case BootStrap.LONG:
                                innerClass += "\n        Long ${it.name};\n"
                                break
                            case BootStrap.FLOAT:
                                innerClass += "\n        Float ${it.name};\n"
                                break
                            case BootStrap.DOUBLE:
                                innerClass += "\n        Double ${it.name};\n"
                                break
                            case BootStrap.BIG_DECIMAL:
                                innerClass += "\n        BigDecimal ${it.name};\n"
                                break
                            case BootStrap.PASSWORD:
                            case BootStrap.STRING:
                                innerClass += "\n        String ${it.name};\n"
                                break
                            case BootStrap.DATE:
                                innerClass += "\n        Date ${it.name};\n"
                                break
                        }
                    }
                    innerClass += "\n       }"
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

        binding.put("innerClass", innerClass)
        binding.put("responseClass", responseClass)
    }

    public static final void genInputActivityBindView(List<InputParam> inputParams, LinkedHashMap<String, String> binding) {
        def bindView = ""

        inputParams.each {
            switch (it.type.name) {
                case BootStrap.INTEGER:
                case BootStrap.LONG:
                case BootStrap.FLOAT:
                case BootStrap.DOUBLE:
                case BootStrap.BIG_DECIMAL:
                case BootStrap.STRING:
                case BootStrap.PASSWORD:
                    bindView += "\n   @BindView(R.id.${it.name}_edit)\n   EditText ${it.name}Edit;"
                    break
                case BootStrap.DATE:
                    bindView += "\n   @BindView(R.id.${it.name}_edit)\n   EditText ${it.name}Edit;"//TODO better use date input view
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
        binding.put("bindView", bindView)
    }

    public static final listItemBindTmpl = """
    @BindView(R.id.\${itemName}_list)
    ListView \${itemName}ListView;

    private List<\${itemClass}> \${itemName}List;
    private \${adapterName}Adapter \${itemName}Adapter;
 """

    public static final void genOutputActivityBindView(List<OutputParam> outputParams, LinkedHashMap<String, String> binding,String inputActivityName) {
        def bindView = ""

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
                    bindView += "\n   @BindView(R.id.${param.name}_result)\n   TextView ${param.name}_Text;"
                    break

//                        BootStrap.FILE
//                        BootStrap.IMAGE
//                        BootStrap.TAKE_PIC
//                        BootStrap.PICK_PIC
//                        BootStrap.VIDEO
//                        BootStrap.TAKE_VIDEO
//                        BootStrap.LOCATION

                case BootStrap.ARRAY:
                    def adapterName = StringUtils.firstCharToUpperCase(param.name)
                    def itemClass =  inputActivityName+"Activity." + adapterName +"Field"

                    final def map = [itemName: param.name, itemClass: itemClass, adapterName: adapterName]
                    bindView += StringUtils.bind(listItemBindTmpl, map)
                    break
                case  BootStrap.OBJECT:
                    param.fields.each { f->
                        switch (f.type.name) {
                            case BootStrap.INTEGER:
                            case BootStrap.LONG:
                            case BootStrap.FLOAT:
                            case BootStrap.DOUBLE:
                            case BootStrap.BIG_DECIMAL:
                            case BootStrap.STRING:
                            case BootStrap.PASSWORD:
                            case BootStrap.DATE:
                                bindView += "\n   @BindView(R.id.${param.name}_${f.name}_result)\n   TextView ${param.name}_${f.name}_Text;"
                                break

                        }
                    }

            }
        }
        binding.put("bindView", bindView)
    }
    public static final genAndroidOutputActivity(String classDir, ServerInterface serverInterface,String inputActivityName, String outputActivityName,
                                                 String outputLayoutName,List<OutputParam> outputParams) {
        final def project = serverInterface.project
        def responseClassName = StringUtils.firstCharToUpperCase(serverInterface.name)
        def binding = [packageName: project.packageName, responseClassName: responseClassName,inputActivityName:inputActivityName,
                       activityName:outputActivityName,outputLayoutName:outputLayoutName,importAdapter:""]
        //gen bindview variables
        genOutputActivityBindView(outputParams, binding,inputActivityName)
        def outputValues = ""
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
                    outputValues += "\n         ${param.name}_Text.setText(response.${param.name}+\"\");"
                    break

//                        BootStrap.FILE
//                        BootStrap.IMAGE
//                        BootStrap.TAKE_PIC
//                        BootStrap.PICK_PIC
//                        BootStrap.VIDEO
//                        BootStrap.TAKE_VIDEO
//                        BootStrap.LOCATION
                 case     BootStrap.ARRAY:
                     String adapterName = StringUtils.firstCharToUpperCase(param.name)
                     def viewHolderBindView = ""
                     def itemValues =""
                     param.fields.each { f->
                         viewHolderBindView += "\n      @BindView(R.id.${f.name}_result) TextView ${f.name}TextView;"
                         itemValues += "\n      holder.${f.name}TextView.setText(item.${f.name}+\"\");"
                     }

                     def itemClass =  inputActivityName+"Activity." + adapterName +"Field"
                     final def itemLayoutName = param.name.toLowerCase() + "_item_layout"
                     def adapterContent = StringUtils.bind(listAdapterClass,[adapterName:adapterName,packageName: project.packageName,
                                                        bindView:viewHolderBindView,itemClass:itemClass,
                                                        itemValues:itemValues, itemLayout: itemLayoutName.toLowerCase(),inputActivityName: inputActivityName+"Activity"])
                     FileUtils.makeClass(classDir,project.packageName+".adapter","${adapterName}Adapter.java",adapterContent)

                     if(binding.get("importAdapter") == null){
                         binding.put("importAdapter","\nimport ${project.packageName}.adapter.${adapterName}Adapter;\n")
                     } else {
                         def oldVal = binding.get("importAdapter")
                         binding.put("importAdapter",oldVal + "\nimport ${project.packageName}.adapter.${adapterName}Adapter;\n")
                     }

                     String itemName = param.name
                     outputValues += "\n        ${itemName}Adapter = new ${adapterName}Adapter(this,response.${itemName});"
                     outputValues += "\n        ${itemName}ListView.setAdapter(${itemName}Adapter);"
                     break;

                case  BootStrap.OBJECT:
                    param.fields.each { f->
                        switch (f.type.name) {
                            case BootStrap.INTEGER:
                            case BootStrap.LONG:
                            case BootStrap.FLOAT:
                            case BootStrap.DOUBLE:
                            case BootStrap.BIG_DECIMAL:
                            case BootStrap.STRING:
                            case BootStrap.PASSWORD:
                            case BootStrap.DATE:
                                outputValues += "\n     ${param.name}_${f.name}_Text.setText(response.${param.name}.${f.name});"
                                break

                        }
                    }
            }
        }
        binding.put("outputValues",outputValues)
        String content = StringUtils.bind(outputActivityClass,binding)
        FileUtils.makeClass(classDir,project.packageName+".activity","${outputActivityName}Activity.java",content)
    }
}
