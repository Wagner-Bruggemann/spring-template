package com.namgrengaw.template.application.adapters.in.controller;

import com.namgrengaw.template.application.adapters.in.docs.AuthControllerDocs;
import com.namgrengaw.template.application.adapters.in.dtos.AccountCredentialsDto;
import com.namgrengaw.template.application.adapters.in.dtos.TokenDto;
import com.namgrengaw.template.application.core.domain.security.AccountCredentials;
import com.namgrengaw.template.application.core.domain.security.Token;
import com.namgrengaw.template.application.core.ports.in.AuthInputGateway;
import com.namgrengaw.template.application.exceptions.InvalidJwtAuthenticationException;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.namgrengaw.template.application.config.mapper.ObjectMapper.parseObject;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    private AuthInputGateway authInputGateway;

    public AuthController(AuthInputGateway service) {
        this.authInputGateway = service;
    }

    @PostMapping(value = "/create-user",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Override
    public AccountCredentialsDto create(@RequestBody AccountCredentialsDto user) {
        final AccountCredentials persistedUser = authInputGateway.create(parseObject(user, AccountCredentials.class));
        return parseObject(persistedUser, AccountCredentialsDto.class);
    }

    @PostMapping("/signin")
    @Override
    public ResponseEntity<TokenDto> signIn(
            @RequestBody AccountCredentialsDto credentials
    ) {
        final Token token = authInputGateway
                .signIn(parseObject(credentials, AccountCredentials.class))
                .orElseThrow(
                        () -> new InvalidJwtAuthenticationException("Invalid client request!")
                );

        return ResponseEntity.ok().body(parseObject(token, TokenDto.class));
    }

    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refresh(
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String refreshToken
    ) {
        final Token token = authInputGateway
                .refresh(username, refreshToken)
                .orElseThrow(
                        () -> new InvalidJwtAuthenticationException("Invalid client request!")
                );

        return ResponseEntity.ok().body(parseObject(token, TokenDto.class));
    }

    private boolean invalidParametersCredentials(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    private static boolean invalidCredentials(AccountCredentialsDto credentials) {
        return credentials == null
                || StringUtils.isBlank(credentials.getUsername())
                || StringUtils.isBlank(credentials.getPassword());
    }

}
