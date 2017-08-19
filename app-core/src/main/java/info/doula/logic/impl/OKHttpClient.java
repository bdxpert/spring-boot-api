package info.doula.logic.impl;

import com.google.gson.Gson;
import info.doula.AppStringUtil;
import info.doula.exception.ServiceConditionException;
import info.doula.logic.CallApi;
import info.doula.system.impl.AppConf;
import okhttp3.*;
import okio.BufferedSink;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hossaindoula on 12/22/2016.
 */
public class OKHttpClient implements CallApi {

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private final Gson gson = new Gson();


    private OkHttpClient authenticatorClient(){
        OkHttpClient client;
        Integer proxyPort = AppStringUtil.convertToZeroOrRealNumber(
                AppConf.getInstanceForMessages().getString("proxy.port"));
        String proxyHost = AppConf.getInstanceForMessages().getString("proxy.host");
        final String username = AppConf.getInstanceForMessages().getString("authenticator.username");
        final String password = AppConf.getInstanceForMessages().getString("authenticator.password");

        Authenticator proxyAuthenticator = new Authenticator() {
            @Override public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            }
        };

        boolean isCacheResponseEnabled = AppConf.getInstance().getBoolean("cache.response.enabled", false);

        if(isCacheResponseEnabled){
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(new File("cache.directory"), cacheSize);
            client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
                    .proxyAuthenticator(proxyAuthenticator)
                    .cache(cache)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
                    .proxyAuthenticator(proxyAuthenticator)
                    .build();
        }

        return client;
    }

    @Override
    public String requestGet(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException {
        return null;
    }

    @Override
    public String requestPost(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException {
        return null;
    }

    @Override
    public String requestPostBody(Map<String, ?> connectionMap, String jsonBody, String contentType) throws ServiceConditionException {
        return null;
    }

    public String requestSynchronousGet(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException, IOException {
        Request request = new Request.Builder()
                .url(connectionMap.get("url").toString())
                .build();
        Response response = authenticatorClient().newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
        }

        return response.body().string();
    }

    public String requestAsynchronousGet(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException, IOException {
        final StringBuffer responseBody = new StringBuffer();
        Request request = new Request.Builder()
                .url(connectionMap.get("url").toString())
                .build();

        authenticatorClient().newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                responseBody.append(response.body().string());
            }
        });

        return responseBody.toString();
    }

    public String requestPostBody(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception{
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        Request request = new Request.Builder()
                .url(connectionMap.get("url").toString())
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

        Response response = authenticatorClient().newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }

    public String requestPostStreaming(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception {
        RequestBody requestBody = new RequestBody() {
            @Override public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };

        Request request = new Request.Builder()
                .url(connectionMap.get("url").toString())
                .post(requestBody)
                .build();

        Response response = authenticatorClient().newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }

    public String requestPostFile(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception {
        File file = new File("README.md");

        Request request = new Request.Builder()
                .url(connectionMap.get("url").toString())
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        Response response = authenticatorClient().newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }

    public String requestPostForm(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception {
        RequestBody formBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url(connectionMap.get("url").toString())
                .post(formBody)
                .build();

        Response response = authenticatorClient().newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }

    public String requestPostMultipart(Map<String, ?> connectionMap, File file, String mimeType)
            throws ServiceConditionException, IOException, Exception {
        final String IMGUR_CLIENT_ID = "...";

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse(mimeType), file))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(connectionMap.get("url").toString())
                .post(requestBody)
                .build();

        Response response = authenticatorClient().newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }
}
