//package user_center.config;
//
//import feign.okhttp.OkHttpClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//public class FeignClientConfig {
//
//    @Bean
//    public okhttp3.OkHttpClient client() {
//        return new okhttp3.OkHttpClient.Builder()
//                .readTimeout(30, TimeUnit.SECONDS)
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .build();
//    }
//
//    @Bean
//    public OkHttpClient feignClient(okhttp3.OkHttpClient client) {
//        return new OkHttpClient(client);
//    }
//}
//
