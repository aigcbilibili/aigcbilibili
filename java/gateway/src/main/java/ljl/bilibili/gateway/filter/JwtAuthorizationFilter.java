package ljl.bilibili.gateway.filter;

import io.jsonwebtoken.Claims;
import ljl.bilibili.gateway.constant.Constant;
import ljl.bilibili.gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static ljl.bilibili.gateway.constant.Constant.JWT_ROLE;
/**
 *自定义过滤器
 **/
//@Component
@Slf4j
public class JwtAuthorizationFilter implements WebFilter {

    /**
     *打印请求路径，用自定义请求头隔绝csrf攻击，取出token认证用户与验证权限
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        //跟踪请求路径
        if (!path.contains("webjars") && !path.contains("swagger") && !path.contains("api")) {
            log.info(exchange.getRequest().getURI().getPath());
        }
//        String jwt = exchange.getRequest().getHeaders().getFirst(Constant.SHORT_TOKEN);
        String aigcBiliBiliHeader = exchange.getRequest().getHeaders().getFirst(Constant.SAFE_REQUEST_HEADER);
        //如果没有该请求头则是非网站源发起的请求
//        if (aigcBiliBiliHeader == null) {
//            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
//            return exchange.getResponse().setComplete();
//        } else {
//            log.info("安全");
//        }
        //如果token不为空则设置权限到security上下文中
//        if (jwt != null) {
//            Claims claims = JwtUtil.getClaimsFromToken(jwt);
//            String role = claims.get(JWT_ROLE, String.class);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(JWT_ROLE + ":" + "user");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "username",
                "password",
                Collections.singletonList(authority)
        );
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(authentication);
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
//        }
        //为空则返回401，需要登录
//        else {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return chain.filter(exchange);


//        }
    }
}


