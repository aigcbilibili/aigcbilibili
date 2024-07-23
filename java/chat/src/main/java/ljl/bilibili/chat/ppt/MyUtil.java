package ljl.bilibili.chat.ppt;

import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.chat.vo.response.ImageResponse;
import okhttp3.HttpUrl;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.text.SimpleDateFormat;

public class MyUtil {
    /**
     * 1.发起post请求
     */
    public static String doPostJson(String url, Map<String, String> urlParams, String json) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse = null;
        String resultString = "";
        ImageResponse imageResponse=null;
        try {
            // 创建Http Post请求
            String asciiUrl = URI.create(url).toASCIIString();
            RequestBuilder builder = RequestBuilder.post(asciiUrl);
            builder.setCharset(StandardCharsets.UTF_8);
            if (urlParams != null) {
                for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            builder.setEntity(entity);
            HttpUriRequest request = builder.build();
            // 执行http请求
            closeableHttpResponse = closeableHttpClient.execute(request);
            System.out.println(closeableHttpResponse.getEntity());
//            System.out.println(closeableHttpResponse);
            resultString = EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8);
            ObjectMapper objectMapper=new ObjectMapper();
             imageResponse=objectMapper.readValue(resultString,ImageResponse.class);
            int i=0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (closeableHttpResponse != null) {
                    closeableHttpResponse.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imageResponse.getPayload().choices.text.get(0).content;
    }
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // date="Thu, 12 Oct 2023 03:05:28 GMT";
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" + "date: " + date + "\n" + "POST " + url.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        // System.err.println(httpUrl.toString());
        return httpUrl.toString();
    }
}
