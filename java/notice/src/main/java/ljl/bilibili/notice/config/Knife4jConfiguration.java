package ljl.bilibili.notice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                                .title("消息模块文档")
                        .description("# 已读未读的点赞、评论消息的获取")
                        .termsOfServiceUrl("https://干.com/")
                        .contact("干")
                        .version("1.0")
                        .build())
                .groupName("消息服务")
                .select()
                .apis(RequestHandlerSelectors.basePackage("ljl.bilibili.notice.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}