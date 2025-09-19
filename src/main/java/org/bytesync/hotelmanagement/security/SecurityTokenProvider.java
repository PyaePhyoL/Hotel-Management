package org.bytesync.hotelmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.exception.InvalidTokenException;
import org.bytesync.hotelmanagement.exception.TokenExpirationForAccessException;
import org.bytesync.hotelmanagement.exception.TokenExpirationForRefreshException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SecurityTokenProvider {

    @Value("${application.security.jwt.issuer}")
    private String issuer;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access}")
    private int access;
    @Value("${application.security.jwt.refresh}")
    private int refresh;

    public enum Type {
        ACCESS, REFRESH
    }

//  generate tokens both access and refresh
    public String generate(Type type, Authentication authentication) {
        var issueAt = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type.name());
        claims.put("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));

        return Jwts.builder()
                .issuer(issuer)
                .claims(claims)
                .subject(authentication.getName())
                .issuedAt(issueAt)
                .expiration(getExpiration(type, issueAt))
                .signWith(getSecretKey())
                .compact();
    }

    public Authentication parse(Type type, String token) {
        try {
            var jws = Jwts.parser().requireIssuer(issuer)
                    .require("type", type.name())
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);

            var username = jws.getPayload().getSubject();
            var role = jws.getPayload().get("roles", String.class);
            var authorities = Arrays.stream(role.split(",")).map(SimpleGrantedAuthority::new).toList();

            return UsernamePasswordAuthenticationToken.authenticated(username, null, authorities );
        } catch (ExpiredJwtException e) {
            throw type == Type.ACCESS
                    ? new TokenExpirationForAccessException()
                    : new TokenExpirationForRefreshException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    public SecretKey getSecretKey() {
        var keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserEmail(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public boolean isTokenValid(String jwt, UserDetails user) {
        var userMail = extractUserEmail(jwt);
        return (userMail.equals(user.getUsername()) && !isTokenExpired(jwt));
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    private Date getExpiration(Type type, Date issueAt) {
        var calendar = Calendar.getInstance();
        calendar.setTime(issueAt);
        calendar.add(Calendar.MINUTE, type == Type.ACCESS ? access : refresh);
        return calendar.getTime();
    }
}
