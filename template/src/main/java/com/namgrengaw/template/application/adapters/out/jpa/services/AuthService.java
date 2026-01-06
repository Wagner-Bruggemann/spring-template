package com.namgrengaw.template.application.adapters.out.jpa.services;

import com.namgrengaw.template.application.adapters.out.jpa.entities.User;
import com.namgrengaw.template.application.adapters.out.jpa.repository.UserRepository;
import com.namgrengaw.template.application.core.domain.security.AccountCredentials;
import com.namgrengaw.template.application.core.domain.security.Token;
import com.namgrengaw.template.application.adapters.out.security.provider.JwtTokenProvider;
import com.namgrengaw.template.application.core.ports.out.AuthOutputGateway;
import com.namgrengaw.template.application.exceptions.RequiredObjectsIsNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements AuthOutputGateway {

    private Logger logger = LoggerFactory.getLogger(AuthService.class);

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private UserRepository repository;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            UserRepository repository
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.repository = repository;
    }

    public Token signIn(AccountCredentials credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getPassword(),
                        credentials.getPassword()
                )
        );

        var user = repository.findByUsername(credentials.getUsername()).
                orElseThrow(() -> new UsernameNotFoundException(
                        "Username " + credentials.getUsername() + " not found!"
                ));

        return tokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());
    }

    public Token refresh(String username, String refreshToken) {
        repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
        return tokenProvider.refreshToken(refreshToken);
    }

    public AccountCredentials create(AccountCredentials user) {
        if (user == null) throw new RequiredObjectsIsNullException();

        logger.info("Creating a user");
        var entity = createUser(user);
        var persistedUser = repository.save(entity);
        return new AccountCredentials(
                persistedUser.getUsername(),
                persistedUser.getName(),
                persistedUser.getPassword().substring(0, 3) + "..."
        );
    }

    private User createUser(AccountCredentials user) {
        var entity = new User();
        entity.setUsername(user.getUsername());
        entity.setName(user.getName());
        entity.setPassword(generateHashedPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);
        return entity;
    }

    private String generateHashedPassword(String password) {
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                "",
                8,
                185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder.encode(password);
    }

}
