package com.boiz.streaming.account.repository;

import com.boiz.streaming.account.constant.RoleType;
import com.boiz.streaming.account.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByType(final RoleType type);

}
