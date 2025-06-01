package com.boiz.streaming.account.repository;

import com.boiz.streaming.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    boolean existsByEmail(final String email);

    Optional<Account> findByEmail(final String email);

}
