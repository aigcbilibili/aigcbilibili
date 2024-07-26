package ljl.bilibili.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "ljl.bilibili.client")
@MapperScan(basePackages = {"ljl.bilibili.mapper","ljl.bilibili.chat.mapper"})
@ComponentScan(basePackages = "ljl.*")
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class,args);
    }
}
