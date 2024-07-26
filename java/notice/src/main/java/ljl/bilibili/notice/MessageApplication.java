package ljl.bilibili.notice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "ljl.bilibili.client")
@MapperScan(basePackages = {"ljl.bilibili.mapper","ljl.bilibili.notice.mapper"})
@ComponentScan(basePackages = "ljl.*")
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class,args);
    }
}
