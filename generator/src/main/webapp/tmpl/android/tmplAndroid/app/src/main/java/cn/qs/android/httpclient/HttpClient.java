package cn.qs.android.httpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import cn.qs.android.httpclient.callback.HttpCallback;
import cn.qs.android.httpclient.callback.OnComplete;
import cn.qs.android.httpclient.callback.OnException;
import cn.qs.android.httpclient.callback.OnFailure;
import cn.qs.android.httpclient.callback.OnSuccess;
import cn.qs.android.httpclient.config.HttpClientConfig;
import cn.qs.android.httpclient.config.DefaultHttpClientConfig;
import cn.qs.android.httpclient.exception.HttpClientException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by 周旭（Troy.Zhou） on 2016/8/29.
 */
public class HttpClient {

    private static Gson gson;
    private static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static Map<String, String> mediaTypes;
    private static String PATH_PARAM_REGEX = "[A-Za-z0-9_\\-/]*\\{[A-Za-z0-9_\\-]+\\}[A-Za-z0-9_\\-/]*";
    private static HttpClientConfig config;
    private static OkHttpClient okHttpClient;
    private static List<Call> calls;

    private TURL turl;
    private boolean synchronous = false;
    private boolean runOnUiThread = false;
    private Map<String, String> headers;
    private Map<String, String> pathParams;
    private Map<String, String> urlParams;
    private Map<String, String> bodyParams;
    private Map<String, String> jsonStrParams;
    private Map<String, Integer> jsonIntParams;
    private Map<String, File> files;
    private String requestJson;
    private HttpCallback callback;
    private OnSuccess onSuccess;
    private OnFailure onFailure;
    private OnException onException;
    private OnComplete onComplete;
    private Call call;
    private HttpLogger logger;

    private HttpClient(TURL turl) {
        this.turl = turl;
        if (config == null) {
            config = new DefaultHttpClientConfig();
        }
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(config.getConnectTimeOutSeconds(), TimeUnit.SECONDS)
                    .readTimeout(config.getReadTimeOutSeconds(), TimeUnit.SECONDS)
                    .writeTimeout(config.getWriteTimeOutSeconds(), TimeUnit.SECONDS);
            SSLSocketFactory sslSocketFactory = config.getSSLSocketFactory();
            X509TrustManager x509TrustManager = config.getX509TrustManager();
            if (sslSocketFactory != null && x509TrustManager != null) {
                builder.sslSocketFactory(sslSocketFactory, x509TrustManager);
            }
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    if (config != null) {
                        return config.hostnameVerifier(hostname);
                    }
                    return true;
                }
            });
            okHttpClient = builder.build();
        }
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            config.configGsonWithBuilder(gsonBuilder);
            gson = gsonBuilder.create();
        }
        if (mediaTypes == null) {
            config.configSupportMediaTypes(mediaTypes = new HashMap<>());
        }
        logger = new HttpLogger(config.isDebugMode(), getClass().getName());
        runOnUiThread = config.runOnUiThread();
    }

    /**
     * 全局配置
     */
    public static void config(HttpClientConfig config) {
        HttpClient.config = config;
    }

    public static HttpClient request(TURL turl) {
        return new HttpClient(turl);
    }

    public HttpClient enableDebug() {
        logger.setEnable(true);
        return this;
    }

    public HttpClient disableDebug() {
        logger.setEnable(false);
        return this;
    }

    public HttpClient addHeader(String name, String value) {
        if (value != null) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(name, value);
        }
        return this;
    }

    /**
     * 资源参数：替换URL里的{name}
     **/
    public HttpClient addPathParam(String name, String value) {
        if (value != null) {
            if (pathParams == null) {
                pathParams = new HashMap<>();
            }
            pathParams.put(name, value);
        }
        return this;
    }

    /**
     * 资源参数：替换URL里的{name}
     **/
    public HttpClient addPathParam(String name, int value) {
        return addPathParam(name, value + "");
    }

    /**
     * URL参数：拼接在URL里的参数
     **/
    public HttpClient addUrlParam(String name, String value) {
        if (value != null) {
            if (urlParams == null) {
                urlParams = new HashMap<>();
            }
            urlParams.put(name, value);
        }
        return this;
    }

    /**
     * Body参数：拼接在URL里的参数
     **/
    public HttpClient addUrlParam(String name, Integer value) {
        if (value != null) {
            if (urlParams == null) {
                urlParams = new HashMap<>();
            }
            urlParams.put(name, value + "");
        }
        return this;
    }

    /**
     * Body参数：拼接在URL里的参数
     **/
    public HttpClient addUrlParam(String name, Long value) {
        if (value != null) {
            if (urlParams == null) {
                urlParams = new HashMap<>();
            }
            urlParams.put(name, value + "");
        }
        return this;
    }

    /**
     * Body参数：放在Body里的参数
     **/
    public HttpClient addBodyParam(String name, String value) {
        if (value != null) {
            if (bodyParams == null) {
                bodyParams = new HashMap<>();
            }
            bodyParams.put(name, value);
        }
        return this;
    }

    /**
     * Body参数：放在Body里的参数
     **/
    public HttpClient addBodyParam(String name, Integer value) {
        if (value != null) {
            if (bodyParams == null) {
                bodyParams = new HashMap<>();
            }
            bodyParams.put(name, value + "");
        }
        return this;
    }


    /**
     * Body参数：放在Body里的参数
     **/
    public HttpClient addBodyParam(String name, Long value) {
        if (value != null) {
            if (bodyParams == null) {
                bodyParams = new HashMap<>();
            }
            bodyParams.put(name, value + "");
        }
        return this;
    }

    /**
     * Json参数：请求体为Json，只支持单层Json
     * 若请求json为多层结构，请使用setRequestJson方法
     */
    public HttpClient addJsonParam(String name, String value) {
        if (value != null) {
            if (jsonStrParams == null) {
                jsonStrParams = new HashMap<>();
            }
            jsonStrParams.put(name, value);
        }
        return this;
    }

    /**
     * Json参数：请求体为Json，只支持单层Json
     * 若请求json为多层结构，请使用setRequestJson方法
     */
    public HttpClient addJsonParam(String name, int value) {
        if (jsonIntParams == null) {
            jsonIntParams = new HashMap<>();
        }
        jsonIntParams.put(name, value);
        return this;
    }

    /**
     * 请求体为json
     **/
    public HttpClient setRequestJson(String json) {
        if (json != null) {
            requestJson = json;
        }
        return this;
    }

    /**
     * 请求体为json
     **/
    public HttpClient setRequestJson(Object json) {
        if (json != null) {
            requestJson = gson.toJson(json);
        }
        return this;
    }

    public HttpClient addFileParam(String name, String filePath) {
        if (filePath != null) {
            if (files == null) {
                files = new HashMap<>();
            }
            files.put(name, new File(filePath));
        }
        return this;
    }

    public HttpClient addFileParam(String name, File file) {
        if (file != null) {
            if (files == null) {
                files = new HashMap<>();
            }
            files.put(name, file);
        }
        return this;
    }

    public HttpClient addFilesParam(Map<String, String> filePaths) {
        if (files == null) {
            files = new HashMap<>();
        }
        for (Map.Entry<String, String> entry : filePaths.entrySet()) {
            files.put(entry.getKey(), new File(entry.getValue()));
        }
        return this;
    }

//    public HttpClient addFilesParam(Map<String, File> files) {
//        if (this.files == null) {
//            this.files = new HashMap<>();
//        }
//        for (Map.Entry<String, File> entry : files.entrySet() ) {
//            this.files.put(entry.getKey(), entry.getValue());
//        }
//        return this;
//    }

    /**
     * 设置为同步请求，默认是异步
     */
    public HttpClient synchronize() {
        synchronous = true;
        return this;
    }

    public HttpClient runOnUiThread() {
        this.runOnUiThread = true;
        return this;
    }

    public HttpClient runOffUiThread() {
        this.runOnUiThread = false;
        return this;
    }

    public HttpClient setCallback(HttpCallback callback) {
        this.callback = callback;
        return this;
    }


    public HttpClient setOnSuccess(OnSuccess onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public HttpClient setOnFailure(OnFailure onFailure) {
        this.onFailure = onFailure;
        return this;
    }

    public HttpClient setOnException(OnException onException) {
        this.onException = onException;
        return this;
    }

    public HttpClient setOnComplete(OnComplete onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public HttpClient get() {
        logger.d("GET request start ...");
        assertNotConflict(true);
        Request.Builder builder = new Request.Builder().url(buildUrlPath());
        buildHeaders(builder);
        builder.get();
        startRequest(builder.build());
        return this;
    }

    public HttpClient post() {
        logger.d("POST request start ...");
        assertNotConflict(false);
        Request.Builder builder = new Request.Builder().url(buildUrlPath());
        buildHeaders(builder);
        builder.post(buildRequestBody());
        startRequest(builder.build());
        return this;
    }

    public HttpClient put() {
        logger.d("PUT request start ...");
        assertNotConflict(false);
        Request.Builder builder = new Request.Builder().url(buildUrlPath());
        buildHeaders(builder);
        builder.put(buildRequestBody());
        startRequest(builder.build());
        return this;
    }

    public HttpClient delete() {
        logger.d("DELETE request start ...");
        assertNotConflict(false);
        Request.Builder builder = new Request.Builder().url(buildUrlPath());
        buildHeaders(builder);
        builder.delete(buildRequestBody());
        startRequest(builder.build());
        return this;
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
            if (calls != null) {
                calls.remove(call);
            }
            logger.i("a http request is canceled");
        }
    }

    public static void cancelAll() {
        if (calls != null) {
            for (Call call : calls) {
                call.cancel();
            }
            calls.clear();
        }
    }

    private void buildHeaders(Request.Builder builder) {
        if (headers != null) {
            for (String name : headers.keySet()) {
                assertNameNotEmpty(name, "header");
                String value = headers.get(name);
                if (value != null) {
                    builder.addHeader(name, value);
                    logger.d("Params [header] : " + name + " = " + value);
                }
            }
        }
    }

    private void startRequest(Request request) {
        call = okHttpClient.newCall(request);
        if (calls == null) {
            calls = new ArrayList<>();
        }
        calls.add(call);
        if (synchronous) {
            logger.d("Synchronous request");
            Response response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                logger.i("A IOException is throwed when executing the request call : " + e.getMessage());
                onException(e);
            }
            onResponse(response);
            calls.remove(call);
        } else {
            logger.d("Asynchronous request");
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    logger.d("A IOException is throwed when enqueueing the request call : " + e.getMessage());
                    AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                        @Override
                        public void call() {
                            onException(e);
                        }
                    });
                    calls.remove(call);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    HttpClient.this.onResponse(response);
                    calls.remove(call);
                }
            });
        }
    }

    private void onResponse(Response response) {
        if (response == null) {
            logger.e("Responsed from server with null and callback was not exceted!");
            return;
        }
        int code = response.code();
        logger.i("Responsed from server with status : " + code);
        logger.d("Responsed [Headers] : " + response.headers());
        String body = null;
        try {
            ResponseBody rbody = response.body();
            if (rbody != null) {
                body = rbody.string();
                logger.d("Response [Body] : " + body);
            } else {
                logger.d("Response [Body] : null");
            }
        } catch (IOException e) {
            logger.i("A IOException is throwed when processing the response : " + e.getMessage());
            onException(e);
            return;
        }
        Headers headers = response.headers();
        if (runOnUiThread) {
            final String finalBody = body;
            AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                @Override
                public void call() {
                    processResponse(code, headers, finalBody);
                }
            });
        } else {
            processResponse(code, headers, body);
        }
    }

    private void onException(IOException e) {
        if (runOnUiThread) {
            AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                @Override
                public void call() {
                    processResException(e);
                }
            });
        } else {
            processResException(e);
        }
    }

    private void prepareCallback() {
        if (callback == null) {
            callback = new HttpCallback() {
                @Override
                public void onSuccess(int status, Headers headers, Object body) {
                    if (onSuccess != null) {
                        onSuccess.onSuccess(status, headers, body);
                    } else {
                        logger.i("[Callback: OnSuccess] : null !");
                    }
                }

                @Override
                public void onFailure(int status, Headers headers, Object body) {
                    if (onFailure != null) {
                        onFailure.onFailure(status, headers, body);
                    } else {
                        logger.i("[Callback: OnFailure] : null !");
                    }
                }

                @Override
                public void onException(Exception e) {
                    if (onException != null) {
                        onException.onException(e);
                    } else {
                        logger.i("Exception [Callback] : null !");
                        logger.w("Exception : a exception was catched : " + e.getMessage());
                    }
                }
            };
        }
    }

    private void processResException(IOException e) {
        prepareCallback();
        logger.d("Exception [Callback] onException will be executed");
        int compCode = OnComplete.EXCEPTION;
        if (e instanceof SocketTimeoutException) {
            compCode = OnComplete.TIMEOUT;
        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
            compCode = OnComplete.NETWORK_ERROR;
        } else if ("Canceled".equals(e.getMessage())) {
            compCode = OnComplete.CANCELED;
        }
        callback.onException(e);
        if (onComplete != null) {
            onComplete.onComplete(compCode);
        }
        logger.d("request end.");
    }

    private void processResponse(int code, Headers headers, String body) {
        prepareCallback();
        int compCode = OnComplete.SUCCESS;
        Object result = null;
        if (code >= 200 && code < 300) {
            Type type = turl.getSuccessReturnType();
            try {
                result = resolveObjectFromBodyString(body, type);
                if(result instanceof ServerResponse){
                    ServerResponse resp = (ServerResponse) result;
                    if(!resp.success){
                        callback.onFailure(code, headers, ((ServerResponse) result).msg);
                        return;
                    } else {
                        logger.d("Response [Callback] onSuccess executing ...");
                        if (result instanceof List && ((List)result).contains(null)) { // Filter null objects
                            List<Object> r = new ArrayList<Object>();
                            for (Object o : (List)result) {
                                if (o != null) {
                                    r.add(o);
                                }
                            }
                            callback.onSuccess(code, headers, r);
                        } else {
                            callback.onSuccess(code, headers, result);
                        }
                        logger.d("Response [Callback] onSuccess executed.");
                    }
                }
            } catch (Exception e) {
                logger.e("ResolveObjectFromBodyException: " + e.getMessage(), e);
                callback.onException(e);
                compCode = OnComplete.EXCEPTION;
            }
        } else {
            Type type = turl.getFailReturnType();
            try {
                result = resolveObjectFromBodyString(body, type);
            } catch (Exception e) {
                logger.e("ResolveObjectFromBodyException: " + e.getMessage(), e);
                callback.onException(e);
                compCode = onComplete.EXCEPTION;
            }
            if (compCode == OnComplete.SUCCESS) {
                logger.d("Response [Callback] onFailure executing ...");
                callback.onFailure(code, headers, result);
                logger.d("Response [Callback] onFailure executed.");
                compCode = OnComplete.FAILURE;
            }
        }
        if (onComplete != null) {
            onComplete.onComplete(compCode);
        }
        logger.d("request end.");
    }

    private Object resolveObjectFromBodyString(String body, Type type) throws Exception {
        logger.d("Response [Callback] ReturnType is [" + (type == null ? "String" : type) + "]");
        Object result = body;
        if (type != null && body != null && !body.trim().isEmpty()) {
            logger.d("Response [Callback] resolving object from json into target type ...");
            result = gson.fromJson(body, type);
            logger.d("Response [Callback] a object resolved successfully!");
        }
        return result;
    }

    private RequestBody buildRequestBody() {
        if (jsonStrParams != null || jsonIntParams != null) {
            requestJson = buildRequestJson();
        }
        if (files != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (bodyParams != null) {
                for (String name : bodyParams.keySet()) {
                    assertNameNotEmpty(name, "bodyParameter");
                    String value = bodyParams.get(name);
                    builder.addFormDataPart(name, value);
                    logger.d("Params [multipart/form] : " + name + " = " + value);
                }
            }
            for (String name : files.keySet()) {
                assertNameNotEmpty(name, "fileParameter");
                File file = files.get(name);
                if (file == null) {
                    throw new HttpClientException("fileParameter 的 file [ " + name + " ] 不能为 null ！");
                }
                if (!file.exists()) {
                    throw new HttpClientException("fileParameter 的 file [ " + name + " ] 不存在！（" + file.getPath() + "）");
                }
                String filename = file.getName();
                String suffix = filename.substring(filename.lastIndexOf(".") + 1);
                MediaType type = parseFileMediaType(suffix);
                builder.addFormDataPart(name, filename, RequestBody.create(type, file));
                logger.d("Params [multipart/form] : " + name + " = " + file.getPath());
            }
            return builder.build();
        } else if (requestJson != null) {
            logger.d("Params [json] : " + requestJson);
            return RequestBody.create(TYPE_JSON, requestJson);
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            if (bodyParams != null) {
                for (String name : bodyParams.keySet()) {
                    assertNameNotEmpty(name, "bodyParameter");
                    String value = bodyParams.get(name);
                    builder.add(name, value);
                    logger.d("Params [form] : " + name + " = " + value);
                }
            }
            return builder.build();
        }
    }

    private String buildRequestJson() {
        String json = "{";
        if (jsonStrParams != null) {
            for (String name : jsonStrParams.keySet()) {
                assertNameNotEmpty(name, "jsonParameter");
                String value = jsonStrParams.get(name);
                if (value != null) {
                    json += "\"" + name + "\":\"" + value + "\",";
                } else {
                    json += "\"" + name + "\":null,";
                }
            }
        }
        if (jsonIntParams != null) {
            for (String name : jsonIntParams.keySet()) {
                assertNameNotEmpty(name, "jsonParameter");
                json += "\"" + name + "\":" + jsonIntParams.get(name) + ",";
            }
        }
        return json.substring(0, json.length() - 1) + "}";
    }

    private String buildUrlPath() {
        String url = turl.getUrlpath();
        if (url == null || url.trim().isEmpty()) {
            throw new HttpClientException("url 不能为空！");
        }
        if (pathParams != null) {
            for (String name : pathParams.keySet()) {
                assertNameNotEmpty(name, "pathParameter");
                String target = "{" + name + "}";
                if (url.contains(target)) {
                    url = url.replace(target, pathParams.get(name));
                } else {
                    throw new HttpClientException("pathParameter [ " + name + " ] 不存在于 url [ " + turl.getUrlpath() + " ]");
                }
            }
        }
        if (url.matches(PATH_PARAM_REGEX)) {
            throw new HttpClientException("url 里有 pathParameter 没有设置，你必须先调用 addPathParam 为其设置！");
        }
        if (urlParams != null) {
            if (url.contains("?")) {
                if (!url.endsWith("?")) {
                    url = url.trim();
                    if (url.lastIndexOf("=") < url.lastIndexOf("?") + 2) {
                        throw new HttpClientException("url 格式错误，'？' 后没有发现 '='");
                    }
                    if (!url.endsWith("&")) {
                        url += "&";
                    }
                }
            } else {
                url += "?";
            }
            for (String name : urlParams.keySet()) {
                assertNameNotEmpty(name, "urlParameter");
                url += name + "=" + urlParams.get(name) + "&";
            }
            url = url.substring(0, url.length() - 1);
        }
        logger.d("URL = " + url);
        return url;
    }


    private void assertNotConflict(boolean isGetRequest) {
        if (isGetRequest) {
            if (requestJson != null) {
                throw new HttpClientException("GET 请求 不能调用 setRequestJson 方法！");
            }
            if (jsonStrParams != null || jsonIntParams != null) {
                throw new HttpClientException("GET 请求 不能调用 addJsonParam 方法！");
            }
            if (bodyParams != null) {
                throw new HttpClientException("GET 请求 不能调用 addBodyParam 方法！");
            }
            if (files != null) {
                throw new HttpClientException("GET 请求 不能调用 addFileParam 方法！");
            }
        }
        if (requestJson != null) {
            if (jsonStrParams != null || jsonIntParams != null) {
                throw new HttpClientException("方法 addJsonParam 与 setRequestJson 不能同时调用！");
            }
            if (bodyParams != null) {
                throw new HttpClientException("方法 addBodyParam 与 setRequestJson 不能同时调用！");
            }
            if (files != null) {
                throw new HttpClientException("方法 addFileParam 与 setRequestJson 不能同时调用！");
            }
        }
        if (jsonStrParams != null || jsonIntParams != null) {
            if (bodyParams != null) {
                throw new HttpClientException("方法 addBodyParam 与 addJsonParam 不能同时调用！");
            }
            if (files != null) {
                throw new HttpClientException("方法 addFileParam 与 addJsonParam 不能同时调用！");
            }
        }
        if (callback != null) {
            if (onSuccess != null) {
                throw new HttpClientException("方法 setCallback 与 setOnSuccess 不能同时调用！");
            }
            if (onFailure != null) {
                throw new HttpClientException("方法 setCallback 与 setOnFailure 不能同时调用！");
            }
            if (onException != null) {
                throw new HttpClientException("方法 setCallback 与 setOnException 不能同时调用！");
            }
        }
    }

    private MediaType parseFileMediaType(String suffix) {
        String type = mediaTypes.get(suffix);
        if (type != null) {
            return MediaType.parse(type);
        }
        logger.i("HttpClient", "." + suffix + " 文件没有找到对应的Content-Type");
        return null;
    }

    private void assertNameNotEmpty(String name, String paramType) {
        if (name == null || name.trim().isEmpty()) {
            throw new HttpClientException(paramType + " 的 name 不可以为空");
        }
    }

}


