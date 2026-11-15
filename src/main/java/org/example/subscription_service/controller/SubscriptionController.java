package org.example.subscription_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.example.subscription_service.model.dto.UserRegisterDto;
import org.example.subscription_service.service.UserService;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/subscription")
@Tag(name = "Управление сервисом подписок")
public class SubscriptionController {
    private final UserService userService;

    @GetMapping("/{login}")
    @Operation(summary = "Получаем пользователя(логин, тип подписки, дата окончания срока подписки)")
    public ResponseEntity<UserRegisterDto> getUser(@PathVariable("id") String login) {
        return ResponseEntity.ok().body(userService.getUser(login));
    }
}
