package com.boiz.streaming.account.entity;

import com.boiz.streaming.account.constant.RoleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "roles", schema = "account")
@Data
@SuperBuilder
@RequiredArgsConstructor
public class Role {

    @Id
    private UUID id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private RoleType type;

}
