package ljl.bilibili.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 *swagger接口文档
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                                .title("聊天模块文档")
                        .description("# 历史聊天对象列表和聊天记录获取")
                        .termsOfServiceUrl("https://你.com/")
                        .contact("你")
                        .version("1.0")
                        .build())
                .groupName("聊天服务")
                .select()
                .apis(RequestHandlerSelectors.basePackage("ljl.bilibili.chat.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}