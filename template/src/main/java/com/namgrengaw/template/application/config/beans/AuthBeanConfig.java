package com.namgrengaw.template.application.config.beans;

import com.namgrengaw.template.application.core.ports.in.AuthInputGateway;
import com.namgrengaw.template.application.core.ports.out.AuthOutputGateway;
import com.namgrengaw.template.application.core.usecases.AuthUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthBeanConfig {

    @Bean
    AuthInputGateway authInputGatewayBean(AuthOutputGateway authOutputGateway) {
        return new AuthUsecase(authOutputGateway);
    }

}
