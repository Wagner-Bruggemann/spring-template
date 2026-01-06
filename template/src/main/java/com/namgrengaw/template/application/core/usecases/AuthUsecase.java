package com.namgrengaw.template.application.core.usecases;

import com.namgrengaw.template.application.core.domain.security.AccountCredentials;
import com.namgrengaw.template.application.core.domain.security.Token;
import com.namgrengaw.template.application.core.ports.in.AuthInputGateway;
import com.namgrengaw.template.application.core.ports.out.AuthOutputGateway;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class AuthUsecase implements AuthInputGateway {

    private AuthOutputGateway authOutputGateway;

    public AuthUsecase(AuthOutputGateway authOutputGateway) {
        this.authOutputGateway = authOutputGateway;
    }

    @Override
    public AccountCredentials create(AccountCredentials accountCredentials) {
        return authOutputGateway.create(accountCredentials);
    }

    @Override
    public Optional<Token> signIn(AccountCredentials accountCredentials) {
        if(invalidCredentials(accountCredentials))
            return Optional.empty();
        return Optional.ofNullable(authOutputGateway.signIn(accountCredentials));
    }

    @Override
    public Optional<Token> refresh(String username, String refreshToken) {
        if(invalidParametersCredentials(username, refreshToken))
            return Optional.empty();
        return Optional.ofNullable(authOutputGateway.refresh(username, refreshToken));
    }

    private boolean invalidParametersCredentials(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    private static boolean invalidCredentials(AccountCredentials credentials) {
        return credentials == null
                || StringUtils.isBlank(credentials.getUsername())
                || StringUtils.isBlank(credentials.getPassword());
    }

}
