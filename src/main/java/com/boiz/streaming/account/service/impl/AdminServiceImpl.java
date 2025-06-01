package com.boiz.streaming.account.service.impl;

import com.boiz.streaming.account.constant.RoleType;
import com.boiz.streaming.account.dto.AccountInfo;
import com.boiz.streaming.account.entity.Account;
import com.boiz.streaming.account.entity.Role;
import com.boiz.streaming.account.exception.CustomException;
import com.boiz.streaming.account.mapper.AccountMapper;
import com.boiz.streaming.account.repository.AccountRepository;
import com.boiz.streaming.account.repository.RoleRepository;
import com.boiz.streaming.account.service.AdminService;
import com.boiz.streaming.account.util.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AccountMapper accountMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public AccountInfo assignManager(final UUID accountId) {
        Account account = findById(accountId);
        Role managerRole = findByType(RoleType.MANAGER);
        account.setRole(managerRole);
        return accountMapper.entityToDto(accountRepository.save(account));
    }

    private Account findById(final UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> CustomException.of(
                messageSource.getMessage(
                        Messages.USER_NOT_FOUND_BY_ID,
                        new Object[] {id},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND
        ));
    }

    private Role findByType(final RoleType type) {
        return roleRepository.findByType(type).orElseThrow(() -> CustomException.of(
                messageSource.getMessage(
                        Messages.ROLE_NOT_FOUND_BY_NAME,
                        new Object[] {type.name()},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND
        ));
    }

}
