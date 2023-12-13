package com.petshop.services.imp;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
@Service
@PropertySource("classpath:application.properties")
public class JwtServiceImp {
    public static Logger logger = LoggerFactory.getLogger(JwtServiceImp.class);
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.secret-key}")
    private  String secretKey ;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,boolean isgenerate
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration,isgenerate);
    }
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateToken(UserDetails userDetails,boolean isgenerate) {
        return generateToken(new HashMap<>(), userDetails,isgenerate);
    }
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration, boolean isgenerate) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (30 * 1000)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private Claims extractAllClaims(String token) {
        try{
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException e){
            logger.error("Failed -> Expried Token Message {}",e.getMessage());
        }catch (MalformedJwtException e){
            logger.error("Failed -> Unsupport Token Message {}",e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("Failed -> Invaid Format Token Message {}",e.getMessage());
        }catch (SignatureException e){
            logger.error("Failed -> Error Signature Token Message {}",e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("Failed -> Claims is Empty Token Message {}",e.getMessage());
        }
        return null;
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }




}
