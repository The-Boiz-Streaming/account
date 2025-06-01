package com.boiz.streaming.account.security;

import com.boiz.streaming.account.entity.Account;
import com.boiz.streaming.account.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String ROLE = "role";
    private static final String PASSWORD = "password";

    private final MessageSource messageSource;
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtTokenProvider(final MessageSource messageSource,
                            @Value("${security.jwt.token.secret.access}") final String jwtAccessSecret,
                            @Value("${security.jwt.token.secret.refresh}") final String jwtRefreshSecret) {
        this.messageSource = messageSource;
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(@NonNull final Account account) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setSubject(account.getEmail())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim(ROLE, account.getRole().getType().name())
                .claim(PASSWORD, account.getPasswordHash())
                .compact();
    }

    public String generateRefreshToken(@NonNull final Account account) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setSubject(account.getEmail())
                .setExpiration(accessExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    public String getEmailFromAccessToken(@NonNull String accessToken) {
        return getAccessClaims(accessToken).getSubject();
    }

    public String getEmailFromRefreshToken(@NonNull String refreshToken) {
        return getRefreshClaims(refreshToken).getSubject();
    }

    public Claims getAccessClaims(@NonNull String accessToken) {
        return getClaims(accessToken, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String refreshToken) {
        return getClaims(refreshToken, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secretKey) {
        try {

            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException expiredJwtException) {

            throw CustomException.of(
                    this.messageSource.getMessage(
                            "jwt.validation.errors.expired",
                            new Object[0],
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.UNAUTHORIZED
            );

        } catch (UnsupportedJwtException unsupportedJwtException) {

            throw CustomException.of(
                    this.messageSource.getMessage(
                            "jwt.validation.errors.unsupported",
                            new Object[0],
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.UNAUTHORIZED
            );

        } catch (MalformedJwtException malformedJwtException) {

            throw CustomException.of(
                    this.messageSource.getMessage(
                            "jwt.validation.errors.malformed",
                            new Object[0],
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.UNAUTHORIZED
            );

        } catch (Exception exception) {

            throw CustomException.of(
                    this.messageSource.getMessage(
                            "jwt.validation.errors.casual.error",
                            new Object[0],
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.UNAUTHORIZED
            );

        }
    }

}
