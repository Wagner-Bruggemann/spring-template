package com.namgrengaw.template.application.core.ports.out;

import com.namgrengaw.template.application.core.domain.security.AccountCredentials;
import com.namgrengaw.template.application.core.domain.security.Token;

public interface AuthOutputGateway {

    AccountCredentials create(AccountCredentials accountCredentials);

    Token signIn(AccountCredentials accountCredentials);

    Token refresh(String username, String refreshToken);

}
