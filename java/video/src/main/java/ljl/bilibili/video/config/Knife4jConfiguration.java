package ljl.bilibili.video.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
@Configuration
@EnableSwagger2WebMvc
@EnableAspectJAutoProxy
public class Knife4jConfiguration {
    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                                .title("视频模块文档")
                        .description("# 首页、视频详情页、个人主页视频数据的获取以及对视频的操作")
                        .termsOfServiceUrl("https://哟.com/")
                        .contact("哟")
                        .version("1.0")
                        .build())
                .groupName("视频服务")
                .select()
                .apis(RequestHandlerSelectors.basePackage("ljl.bilibili.video.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}