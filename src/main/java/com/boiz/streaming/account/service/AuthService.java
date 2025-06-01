package com.boiz.streaming.account.service;

import com.boiz.streaming.account.dto.*;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AccountInfo signup(final Signup dto);

    TokenPair login(final Login dto, HttpServletResponse response);

    AccessToken getAccessToken(final RefreshToken dto);

    void logout(final Logout dto);

}
