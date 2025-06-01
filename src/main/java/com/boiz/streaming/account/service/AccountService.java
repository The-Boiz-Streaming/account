package com.boiz.streaming.account.service;

import com.boiz.streaming.account.dto.AccountInfo;
import com.boiz.streaming.account.entity.Account;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface AccountService {

    AccountInfo getAccountInfo(final UUID id);

    AccountInfo getCurrentAccountInfo(final HttpServletRequest request);

    AccountInfo likeRelease(final HttpServletRequest request, final UUID releaseId);

    AccountInfo likeTrack(final HttpServletRequest request, final UUID trackId);

}
