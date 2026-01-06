package com.namgrengaw.template.application.core.ports.in;

import com.namgrengaw.template.application.core.domain.security.AccountCredentials;
import com.namgrengaw.template.application.core.domain.security.Token;

import java.util.Optional;

public interface AuthInputGateway {

    AccountCredentials create(AccountCredentials accountCredentials);

    Optional<Token> signIn(AccountCredentials accountCredentials);

    Optional<Token> refresh(String username, String refreshToken);
}
