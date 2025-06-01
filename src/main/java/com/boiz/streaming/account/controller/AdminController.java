package com.boiz.streaming.account.controller;

import com.boiz.streaming.account.dto.AccountInfo;
import com.boiz.streaming.account.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/assign-manager/{id}")
    public ResponseEntity<?> assignManager(@PathVariable("id") final UUID accountId) {
        AccountInfo info = adminService.assignManager(accountId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

}
