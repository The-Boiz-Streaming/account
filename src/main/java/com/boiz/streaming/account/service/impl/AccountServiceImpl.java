package com.boiz.streaming.account.service.impl;

import com.boiz.streaming.account.dto.AccountInfo;
import com.boiz.streaming.account.entity.Account;
import com.boiz.streaming.account.exception.CustomException;
import com.boiz.streaming.account.mapper.AccountMapper;
import com.boiz.streaming.account.repository.AccountRepository;
import com.boiz.streaming.account.security.JwtTokenProvider;
import com.boiz.streaming.account.service.AccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.boiz.streaming.account.util.Messages.USER_NOT_FOUND_BY_EMAIL;
import static com.boiz.streaming.account.util.Messages.USER_NOT_FOUND_BY_ID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final String BEARER = "Bearer ";

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final MessageSource messageSource;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AccountInfo getAccountInfo(final UUID id) {
        return accountRepository.findById(id)
                .map(accountMapper::entityToDto)
                .orElseThrow(() -> CustomException.of(
                        messageSource.getMessage(
                                USER_NOT_FOUND_BY_ID,
                                new Object[] {id},
                                LocaleContextHolder.getLocale()
                        ), HttpStatus.NOT_FOUND
                ));
    }

    @Override
    public AccountInfo getCurrentAccountInfo(final HttpServletRequest request) {
        final String email = getEmailFromHttpRequest(request);
        return accountMapper.entityToDto(findByEmail(email));
    }

    @Override
    @Transactional
    public AccountInfo likeRelease(final HttpServletRequest request, final UUID releaseId) {
        final String email = getEmailFromHttpRequest(request);
        Account account = findByEmail(email);
        account.addLikedRelease(releaseId);
        return accountMapper.entityToDto(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountInfo likeTrack(final HttpServletRequest request, final UUID trackId) {
        final String email = getEmailFromHttpRequest(request);
        Account account = findByEmail(email);
        account.addLikedTrack(trackId);
        return accountMapper.entityToDto(accountRepository.save(account));
    }

    private Account findByEmail(final String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> CustomException.of(
                        messageSource.getMessage(
                                USER_NOT_FOUND_BY_EMAIL,
                                new Object[] {email},
                                LocaleContextHolder.getLocale()
                        ), HttpStatus.NOT_FOUND
                ));
    }

    private String getEmailFromHttpRequest(HttpServletRequest request) {
        String token;
        Cookie authCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("AuthToken")) {
                    authCookie = cookie;
                }
            }
        }

        if (authCookie != null && !authCookie.getValue().isEmpty()) {
            token = authCookie.getValue();
        } else {
            token = request.getHeader(HttpHeaders.AUTHORIZATION);
        }

        if (token != null && token.startsWith(BEARER)) {
            token = token.substring(BEARER.length());
        }

        if (token != null && jwtTokenProvider.validateAccessToken(token)) {
            return jwtTokenProvider.getEmailFromAccessToken(token);
        }

        return null;
    }

}
