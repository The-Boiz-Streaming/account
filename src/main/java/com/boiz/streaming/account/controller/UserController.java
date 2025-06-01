package com.boiz.streaming.account.controller;

import com.boiz.streaming.account.dto.AccountInfo;
import com.boiz.streaming.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class UserController {

    private final AccountService accountService;

    @GetMapping("/current")
    public ResponseEntity<?> getInfo(final HttpServletRequest request) {
        AccountInfo info = accountService.getCurrentAccountInfo(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInfo(@PathVariable("id") final UUID id) {
        AccountInfo info = accountService.getAccountInfo(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    @PatchMapping("/like/track/{id}")
    public ResponseEntity<?> likeTrack(final HttpServletRequest request,
                                       @PathVariable("id") final UUID trackId) {
        AccountInfo info = accountService.likeTrack(request, trackId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    @PatchMapping("/like/release/{id}")
    public ResponseEntity<?> likeRelease(final HttpServletRequest request,
                                         @PathVariable("id") final UUID releaseId) {
        AccountInfo info = accountService.likeRelease(request, releaseId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

}
