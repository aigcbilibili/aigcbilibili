package ljl.bilibili.gateway.config;
import ljl.bilibili.gateway.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.stream.Collectors;
/**
 *Security鉴权
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     *放行路径与权限要求，添加自定义权限校验器，不交与Spring管理解决重复经过过滤器的问题
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                //放行路径
                .pathMatchers( "/webjars/","/register/**","/user-center/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/**","/video/**","searchs/**","notice/**","/chats/**",
                        "/getAllTableData/**","/getComment/**","/getDanmaku/**"
                        ,"/getHistoryVideo/**","/getDetailVideo/**","/getCommendVideo/**","/getFirstPageVideo/**"
                        ,"/getEnsembleVideo/**","/getAllVideoInEnsemble/**","/getUserPrivilege/**",
                        "/getPersonalVideo/**","/getRemotelyLikeVideo/**","/getUserInfo/**"
                        ,"/search/**"
                ).permitAll()
                //其他路径需要有role:user权限
                .anyExchange().hasAuthority("role:user")
                .and()
                //禁用http基础认证
                .httpBasic().disable()
                //禁用表单认证
                .formLogin().disable()
                //使用自定义过滤器替换默认过滤器
                .addFilterAt(new JwtAuthorizationFilter(),SecurityWebFiltersOrder.AUTHORIZATION)
                //禁止csrf令牌防护
                .csrf().disable();
        return http.build();
    }
    /**
     *密码加密与解密工具
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     *用到openfeign远程调用则需要手动注入该bean，因为webflux和mvc风格不同，默认不注入该bean
     **/
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
