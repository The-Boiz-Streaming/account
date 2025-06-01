package com.boiz.streaming.account.mapper;

import com.boiz.streaming.account.dto.AccountInfo;
import com.boiz.streaming.account.dto.Signup;
import com.boiz.streaming.account.entity.Account;
import com.boiz.streaming.account.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "role", expression = "java(getRoleInStr(account.getRole()))")
    AccountInfo entityToDto(Account account);

    @Mapping(target = "likedTracks", expression = "java(getDefaultLikesMap())")
    @Mapping(target = "likedReleases", expression = "java(getDefaultLikesMap())")
    Account dtoToEntity(Signup dto);

    default String getRoleInStr(final Role role) {
        return role.getType().name();
    }

    default Set<UUID> getDefaultLikesMap() {
        return new HashSet<>();
    }
}
