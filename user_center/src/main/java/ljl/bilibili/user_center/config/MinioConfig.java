package ljl.bilibili.user_center.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 *minio配置类注册miniocCient
 */
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String endpoint;

    @Value("${minio.access.name}")
    private String accessKey;

    @Value("${minio.access.secret}")
    private String secretKey;


    @Bean
    public MinioClient minioClient() {
        try {
            return MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
