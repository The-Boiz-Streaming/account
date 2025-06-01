package com.boiz.streaming.account.service;

import com.boiz.streaming.account.dto.AccountInfo;

import java.util.UUID;

public interface AdminService {

     AccountInfo assignManager(final UUID accountId);

}
