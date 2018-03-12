package cn.qs.android.httpclient.config;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.qs.android.httpclient.HttpLogger;
import cn.qs.android.httpclient.exception.HttpClientSSLConfigException;

/**
 * Created by 周旭（Troy.Zhou） on 2016/8/31.
 *
 * SSLHttpClientConfig 用于让 HttpClient 可以访问自产证书的https的连接
 * 证书可以浏览器上导出，导时选择X509，导出结果的xxx.cer。
 * 使用时需把导出的 .cer 证书存放到约定目录Assets/certs下
 * 然后再访问Https前配置如下即可。
 * <pre>
 *      HttpClient.config(SSLHttpClientConfig.defaultSSLConfig([上下文对象]));
 * </pre>
 *
 * 若要实现双向认证，需要把 .bks 证书也存放到约定目录Assets/certs下
 * 然后再访问Https前配置如下即可。
 * <pre>
 *      HttpClient.config(SSLHttpClientConfig.twoWayAuthConfig([上下文对象], "[BKS 密码]"));
 * </pre>
 *
 * 若要信任所有证书，只需如下配置：
 * <pre>
 *      HttpClient.config(SSLHttpClientConfig.trustAllConfig());
 * </pre>
 *
 */
public class SSLHttpClientConfig extends DefaultHttpClientConfig {

    private Context context;
    private HttpLogger logger;

    private X509TrustManager x509TrustManager;
    private SSLSocketFactory sslSocketFactory;

    private boolean trustAll = false;

    private String certDirectory = "certs";
    private String bksDirectory = null;
    private String passwordToEnableBKS = null;


    public SSLHttpClientConfig() {
    }

    /**
     *  信任所有自产证书，安全系数低
     * */
    public static SSLHttpClientConfig trustAllConfig() {
        SSLHttpClientConfig config = new SSLHttpClientConfig();
        config.setTrustAll(true);
        return config;
    }

    /**
     *  信任指定证书，
     *  需要把 .cer 证书存放到约定目录Assets/certs下
     * */
    public static SSLHttpClientConfig defaultSSLConfig(Context context) {
        SSLHttpClientConfig config = new SSLHttpClientConfig();
        config.setContext(context);
        return config;
    }

    /**
     * 双向认证，安全系数高
     * 需要把 .cer 和 .bks 证书都存放到约定目录Assets/certs下
     * */
    public static SSLHttpClientConfig twoWayAuthConfig(Context context, String password) {
        SSLHttpClientConfig config = new SSLHttpClientConfig();
        config.setContext(context);
        config.setPasswordToEnableBKS(password);
        return config;
    }

    /**
     * 放有证书的相对于 assets 的目录
     * 默认为 certs 目录
     */
    public String getCertDirectory() {
        return certDirectory;
    }

    public String getBksDirectory() {
        return getCertDirectory();
    }

    /**
     * 双向认证bks密码
     * @return null 闭关双向认证，否则 打开双向认证，同时必须在存在证书的目录下放置 .bks 文件
     * */
    public String getPasswordToEnableBKS() {
        return passwordToEnableBKS;
    }

    /**
     * 信任所有证书，危险比较高
     * */
    public boolean isTrustAll() {
        return trustAll;
    }

    public void setCertDirectory(String certDirectory) {
        this.certDirectory = certDirectory;
    }

    public void setBksDirectory(String bksDirectory) {
        this.bksDirectory = bksDirectory;
    }

    public void setPasswordToEnableBKS(String passwordToEnableBKS) {
        this.passwordToEnableBKS = passwordToEnableBKS;
    }

    /**
     * 信任所有证书，危险比较高
     * */
    public void setTrustAll(boolean trustAll) {
        this.trustAll = trustAll;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    final public X509TrustManager getX509TrustManager() {
        if (x509TrustManager == null) {
            buildSSLSocketFactoryAndX509TrustManager();
        }
        return x509TrustManager;
    }

    @Override
    final public SSLSocketFactory getSSLSocketFactory() {
        if (sslSocketFactory == null) {
            buildSSLSocketFactoryAndX509TrustManager();
        }
        return sslSocketFactory;
    }

    private List<InputStream> openInputStreams(String directory, String suffix) {
        if (directory == null) {
            directory = "";   // assets 跟目录
        }
        if (context == null) {
            throw new HttpClientSSLConfigException("必须要传入 context 参数！");
        }
        AssetManager assetManager = context.getAssets();
        List<InputStream> certInputStreams = new ArrayList<>();
        try {
            List<String> certAssets = new ArrayList<>();
            String[] assets = assetManager.list(directory);
            if (assets != null && assets.length > 0) {
                for (String asset : assets) {
                    if (asset != null) {
                        if (asset.endsWith(suffix)) {
                            certAssets.add(asset);
                        }
                    }
                }
            }
            for (String certAsset : certAssets) {
                certInputStreams.add(assetManager.open(directory + certAsset));
            }
        } catch (IOException e) {
            throw new HttpClientSSLConfigException(e.getMessage(), e);
        }
        logger.d("读取到 " + certInputStreams.size() + " 个 " + suffix + " 文件");
        return certInputStreams;
    }

    private KeyStore buildKeyStoreWithCertInputStreams(List<InputStream> certInputStreams, InputStream bksInputStream, char[] passwordCharArray) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(bksInputStream, passwordCharArray);
            for (int i = 0, size = certInputStreams.size(); i < size; ) {
                InputStream certificate = certInputStreams.get(i);
                String certificateAlias = Integer.toString(i++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                if (certificate != null) {
                    certificate.close();
                }
            }
            if (bksInputStream != null) {
                bksInputStream.close();
            }
            return keyStore;
        }  catch (Exception e) {
            throw new HttpClientSSLConfigException(e.getMessage(), e);
        }
    }

    private void buildSSLSocketFactoryAndX509TrustManager() {
        if (logger == null) {
            logger = new HttpLogger(isDebugMode(), getClass().getName());
        }
        if (isTrustAll())  {
            buildSSLSocketFactoryAndX509TrustManagerWithTrustAll();
        } else {
            buildSSLSocketFactoryAndX509TrustManagerWithCertificates();
        }
    }

    private void buildSSLSocketFactoryAndX509TrustManagerWithTrustAll() {
        try {
            x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)throws CertificateException { }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{ x509TrustManager }, new SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            throw new HttpClientSSLConfigException(e.getMessage(), e);
        }
    }

    private void buildSSLSocketFactoryAndX509TrustManagerWithCertificates() {
        List<InputStream> certInputStreams = openInputStreams(getCertDirectory(), ".cer");
        if (certInputStreams.isEmpty()) {
            logger.e("没有找到 .cer 文件！");
            return;
        }
        String password = getPasswordToEnableBKS();
        logger.e("bks password is " + password);
        KeyStore keyStore = null;
        if (password != null) {
            List<InputStream> bksInputStreams = openInputStreams(getBksDirectory(), ".bks");
            if (bksInputStreams.size() == 0) {
                throw new HttpClientSSLConfigException("您设置了BKS密码，您必须同时在存在 .cer 证书的目录下放置 .bks 证书！");
            }
            if (bksInputStreams.size() > 1) {
                throw new HttpClientSSLConfigException("检测到多个 BKS 证书！");
            }
            logger.e("builing keyStore with .cer and .bks files");
            keyStore = buildKeyStoreWithCertInputStreams(certInputStreams, bksInputStreams.get(0), password.toCharArray());
        } else {
            logger.e("builing keyStore with .cer files");
            keyStore = buildKeyStoreWithCertInputStreams(certInputStreams, null, null);
        }
        logger.e("a keyStore is built!");
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            x509TrustManager = findX509TrustManager(trustManagers);
            logger.e("a x509TrustManager is built!");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            if (password != null) {
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, password.toCharArray());
                sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[]{x509TrustManager}, new SecureRandom());
            } else {
                sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            }
            sslSocketFactory = sslContext.getSocketFactory();
            logger.e("a sslSocketFactory is built!");
        } catch (Exception e) {
            throw new HttpClientSSLConfigException(e.getMessage(), e);
        }
    }

    private X509TrustManager findX509TrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager: trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager)trustManager;
            }
        }
        throw new IllegalStateException("Unexpected default trust managers:"
                + Arrays.toString(trustManagers));
    }


}


