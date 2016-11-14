package com.team.demo.api;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.team.demo.App;
import com.team.demo.api.response.APIErrorResponse;
import com.team.demo.ourlibrary.BuildConfig;
import com.team.demo.ourlibrary.utils.JsonUtils;
import com.team.demo.ourlibrary.utils.StringUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by Ocean on 6/7/16.
 */
@SuppressWarnings("all")
public class ServiceGenerator extends Activity {
    private final static String BASE_URL = "https://api.douban.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//            BuildConfig.BASE_URL.equalsIgnoreCase("http://api.qg.com/adviser/v1/") ?
//                    new OkHttpClient.Builder() :
//                    new OkHttpClient.Builder().sslSocketFactory(getSSLSocketFactory(getCer())).hostnameVerifier(getHostnameVerifier());

    private static Retrofit retrofit;

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()));


//    private static int getCer() {
//        //FORMAL:正式环境;DEVELOP:开发环境;PRODUCT:预生产环境;
//        switch (BuildConfig.IDE) {
//            case "EMULATION":
//                return R.raw.i_wengu_com;
//            case "FORMAL":
//                return R.raw.iwengu_cn;
//        }
//        return 0;
//    }


    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <S> S createRetrofitService(Class<S> serviceClass) {

        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request originalReq = chain.request();

                String sortJson = "";

                if (originalReq.body() != null) {
                    Buffer buffer = new Buffer();
                    originalReq.body().writeTo(buffer);
                    String reqJsonBody = buffer.readUtf8();
                    if (!StringUtils.isNullOrEmpty(reqJsonBody)) {
                        try {
                            JSONObject obj = new JSONObject(reqJsonBody);
                            Map<String, Object> map = JsonUtils.jsonToMap(obj);
                            LinkedHashMap<String, Object> sortedMap = JsonUtils.sortMapByKey(map);
                            Gson gson = new Gson();
                            sortJson = gson.toJson(sortedMap);
                        } catch (Exception e) {

                        }
                    }
                }

                //请求头
//                AppConfig config = AppConfig.sharedInstance();
//
//
//                String subPath = String.format("/%s", originalReq.url().url().toString().replaceFirst(BASE_URL, ""));
//                String timestamp = Long.toString(System.currentTimeMillis());
//                String sign = String.format("%s%s%s%s", config.getApiSecurity(), timestamp, subPath, sortJson);
//                String deviceInfo = config.getDeviceType() + "<>" + config.getDeviceToken() + "<>" + config.getAppVersion() + "<>" + config.getRegistId();
//
//                String signSha1 = IWGUtils.sha1(sign.getBytes());
//
//                Log.d("origianSign", sign);
//                Log.d("sign", signSha1);
//                Log.d("api-key", config.getApiKey());
//                Log.d("timestamp", timestamp);
//                Log.d("di", deviceInfo);
//
                int maxAge = 0; // read from cache
                Request request = originalReq.newBuilder()
//                        .header("Content-Type", "application/json")
//                        .header("Accept", "application/json")
//                        .header("api-key", config.getApiKey())
//                        .header("timestamp", timestamp)
//                        .header("sign", signSha1)
//                        .header("-di-", deviceInfo)
//                        .method(originalReq.method(), originalReq.body())
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();

                return chain.proceed(request);
            }
        });

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(loggingInterceptor);
        }

        OkHttpClient client = httpClient.build();
        retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);

    }


    protected static SSLSocketFactory getSSLSocketFactory(int cer) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        SSLContext sslContext = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream cert = App.getInstance().getResources().openRawResource(cer);
            Certificate ca;
            try {
                ca = cf.generateCertificate(cert);
                Log.i("Longer", "ca=" + ((X509Certificate) ca).getSubjectDN());
                Log.i("Longer", "key=" + ((X509Certificate) ca).getPublicKey());
            } finally {
                cert.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore kStore = KeyStore.getInstance(keyStoreType);
            kStore.load(null, null);
            kStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(kStore);

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        } catch (CertificateException |
                KeyStoreException
                | NoSuchAlgorithmException
                | IOException
                | KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }


    protected static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                boolean ret = false;
                if ("api...".equalsIgnoreCase(hostname)) {
                    ret = true;
                } else
                    ret = false;
                return ret;
            }
        };
        return TRUSTED_VERIFIER;
    }

    public static APIErrorResponse parseError(retrofit2.Response<?> response) {

        Converter<ResponseBody, APIErrorResponse> converter = ServiceGenerator.getRetrofit().responseBodyConverter(APIErrorResponse.class, new Annotation[0]);

        APIErrorResponse result = new APIErrorResponse();
        result.setCode(String.valueOf(response.code()));
        result.setMsg(response.message());

        if (response.code() == 400) {
            try {
                result = converter.convert(response.errorBody());
            } catch (IOException e) {

            }
        }

        return result;
    }

    public static APIErrorResponse handle(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException error = (HttpException) throwable;
            APIErrorResponse ret = new APIErrorResponse();
            ret.setCode(String.format("%s", error.code()));
            ret.setMsg(error.getLocalizedMessage());
            return ret;
        } else {
            APIErrorResponse ret = new APIErrorResponse();
            ret.setCode("000000");
            ret.setMsg("请求失败");
            return ret;
        }
    }

}
