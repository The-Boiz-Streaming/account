package com.boiz.streaming.account.service.impl;

import com.boiz.streaming.account.cache.RefreshTokenCache;
import com.boiz.streaming.account.constant.RoleType;
import com.boiz.streaming.account.dto.*;
import com.boiz.streaming.account.entity.Account;
import com.boiz.streaming.account.exception.CustomException;
import com.boiz.streaming.account.mapper.AccountMapper;
import com.boiz.streaming.account.repository.AccountRepository;
import com.boiz.streaming.account.repository.RoleRepository;
import com.boiz.streaming.account.security.JwtTokenProvider;
import com.boiz.streaming.account.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String AUTH_TOKEN = "AuthToken";

    @Value("${security.admin.email}")
    private String adminEmail;
    @Value("${security.admin.password}")
    private String adminPassword;

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenCache refreshTokenCache;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public AccountInfo signup(final Signup dto) {
        Account account = accountMapper.dtoToEntity(dto);

        if (accountRepository.existsByEmail(account.getEmail())) {
            throw CustomException.of(
                    this.messageSource.getMessage(
                            "user.auth.errors.email.already.exists",
                            new Object[]{account.getEmail()},
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        if (account.getEmail().equals(adminEmail) && dto.password().equals(adminPassword)) {
            account.setRole(
                    roleRepository.findByType(RoleType.ADMIN)
                            .orElseThrow(() -> CustomException.of(
                                    this.messageSource.getMessage(
                                            "user.auth.errors.role.not.found.by.name",
                                            new Object[]{RoleType.ADMIN.name()},
                                            LocaleContextHolder.getLocale()
                                    ),
                                    HttpStatus.NOT_FOUND
                            ))
            );
        } else {
            account.setRole(
                    roleRepository.findByType(RoleType.USER)
                            .orElseThrow(() -> CustomException.of(
                                    this.messageSource.getMessage(
                                            "user.auth.errors.role.not.found.by.name",
                                            new Object[]{RoleType.USER.name()},
                                            LocaleContextHolder.getLocale()
                                    ),
                                    HttpStatus.NOT_FOUND
                            ))
            );
        }

        final String passwordEncoded = passwordEncoder.encode(dto.password());
        account.setPasswordHash(passwordEncoded);

        account = accountRepository.save(account);

        return accountMapper.entityToDto(account);
    }

    @Override
    public TokenPair login(final Login dto,
                           HttpServletResponse response) {
        final String email = dto.email();
        final String rawPassword = dto.password();

        final Account account = findAccountByEmail(email);

        if (passwordEncoder.matches(rawPassword, account.getPasswordHash())) {
            final String accessToken = jwtTokenProvider.generateAccessToken(account);
            final String refreshToken = jwtTokenProvider.generateRefreshToken(account);

            refreshTokenCache.save(account.getEmail(), refreshToken);

            setAuthTokenCookie(response, accessToken);

            return TokenPair.of(accessToken, refreshToken);
        }

        throw CustomException.of(
                this.messageSource.getMessage(
                        "user.auth.errors.incorrect.password",
                        new Object[]{account.getEmail()},
                        LocaleContextHolder.getLocale()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @Override
    public AccessToken getAccessToken(final RefreshToken dto) {
        final String refreshToken = dto.refreshToken();

        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            final String email = jwtTokenProvider.getEmailFromRefreshToken(refreshToken);
            final String savedRefreshToken = getSavedRefreshTokenFromEmail(email);

            if (Objects.nonNull(savedRefreshToken) && savedRefreshToken.equals(refreshToken)) {
                final Account account = findAccountByEmail(email);
                final String accessToken = jwtTokenProvider.generateAccessToken(account);
                return AccessToken.of(accessToken);
            }
        }

        throw CustomException.of(
                this.messageSource.getMessage(
                        "user.auth.errors.invalid.refresh.token",
                        new Object[0],
                        LocaleContextHolder.getLocale()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @Override
    public void logout(final Logout dto) {
        setAuthTokenCookie(dto.response(), "");
    }

    private void setAuthTokenCookie(HttpServletResponse response,
                                    final String accessToken) {
        Cookie cookie = new Cookie(AUTH_TOKEN, accessToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        response.addCookie(cookie);
    }

    private String getSavedRefreshTokenFromEmail(final String email) {
        return refreshTokenCache.get(email).orElseThrow(() -> CustomException.of(
                this.messageSource.getMessage(
                        "user.auth.errors.no.refresh.token",
                        new Object[] {email},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.INTERNAL_SERVER_ERROR
        ));
    }

    private Account findAccountByEmail(final String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> CustomException.of(
                        this.messageSource.getMessage(
                                "user.auth.errors.user.not.found.by.email",
                                new Object[]{email},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));
    }
}
