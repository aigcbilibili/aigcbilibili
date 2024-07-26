package ljl.bilibili.user_center.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("用户模块文档")
                        .description("# 用户个人信息查看与编辑，用户开放权限查看与编辑以及对up主的操作")
                        .termsOfServiceUrl("https://哎.com/")
                        .contact("哎")
                        .version("1.0")
                        .build())
                .groupName("用户服务")
                .select()
                .apis(RequestHandlerSelectors.basePackage("ljl.bilibili.user_center.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}