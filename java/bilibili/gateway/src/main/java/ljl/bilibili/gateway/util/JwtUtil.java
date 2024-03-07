package ljl.bilibili.gateway.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static ljl.bilibili.gateway.constant.Constant.*;
@Slf4j
public class JwtUtil {

    /**
     *生成短jwt令牌
     */
    public static String generateShortToken(Integer userId) {
        return generateToken(userId, SHORT_TOKEN_EXPIRATION);
    }
    /**
     *生成长jwt令牌
     */
    public static String generateLongToken(Integer userId) {
        return generateToken(userId, LONG_TOKEN_EXPIRATION);
    }
    /**
     *生成jwt令牌
     */
    private static String generateToken(Integer userId, int expirationTime) {
        Map<String, Object> claims = new HashMap<>(1);
        claims.put(JWT_ROLE, JWT_ROLE_NAME);
        claims.put(USERIDENTITY,userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(TOKEN_SUBJECT)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
    /**
     *校验jwt令牌是否合法
     */
    public static boolean validateToken(String token) {
        try {
            Date date=new Date();
            Date tokenExpirationTime=Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
            if(date.getTime()-tokenExpirationTime.getTime()>0){
                return false;
            }
            return true;
        } catch (ExpiredJwtException | SignatureException e) {
            log.info("jwtError");
            return false;
        } catch (JwtException e) {
            log.info("jwtError");
            return false;
        }
    }
    /**
     *刷新token
     */
    public static String refreshToken(String longToken,Integer type) {
        if (validateToken(longToken)) {
            Integer userId = getClaimsFromToken(longToken).get(USERIDENTITY,Integer.class);
            if(type==0){
                return generateShortToken(userId);
            }else {
                return generateLongToken(userId);
            }
        } else {
            throw new JwtException("Invalid long token");
        }
    }
}
