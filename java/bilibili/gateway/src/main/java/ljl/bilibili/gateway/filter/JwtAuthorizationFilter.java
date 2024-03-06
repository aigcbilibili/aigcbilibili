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

//@Component
@Slf4j
public class JwtAuthorizationFilter implements WebFilter {

    /**
     *打印请求路径，用自定义请求头隔绝csrf攻击，取出token认证用户与验证权限
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (!path.contains("webjars") && !path.contains("swagger") && !path.contains("api")) {
            log.info(exchange.getRequest().getURI().getPath());
        }
        String jwt = exchange.getRequest().getHeaders().getFirst(Constant.SHORT_TOKEN);
        String laBiliBiliHeader = exchange.getRequest().getHeaders().getFirst(Constant.SAFE_REQUEST_HEADER);
        if (laBiliBiliHeader == null) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        } else {
            log.info("yuyu");
        }
        if (jwt != null) {
            Claims claims = JwtUtil.getClaimsFromToken(jwt);
            String role = claims.get(JWT_ROLE, String.class);
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(JWT_ROLE + ":" + role);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    "username",
                    "password",
                    Collections.singletonList(authority)
            );
            SecurityContext context = new SecurityContextImpl();
            context.setAuthentication(authentication);
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
            return chain.filter(exchange);

        }

        }


}

//            return ReactiveSecurityContextHolder.getContext()
//                    .map(securityContext -> {
//                        securityContext.setAuthentication(authentication);
//                        return securityContext;
//                    })
//                    .flatMap(securityContext -> {
//                        return chain.filter(exchange);
//                    });
//        }

//        return chain.filter(exchange);

