package com.app.reddit.service;

import com.app.reddit.dto.RegisterRequest;
import com.app.reddit.exception.ActivationException;
import com.app.reddit.models.AccountVerificationToken;
import com.app.reddit.models.NotificationEmail;
import com.app.reddit.models.User;
import com.app.reddit.repository.TokenRepository;
import com.app.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.app.reddit.config.Constants.EMAIL_ACTIVATION;

@Service
@AllArgsConstructor
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    TokenRepository tokenRepository;
    MailService mailService;
    MailBuilder mailBuilder;

    // Exception mailService.sendEmail
    @SneakyThrows
    @Transactional
    public void register (RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreationDate(Instant.now());
        user.setAccountStatus(false);

        userRepository.save(user);

        String token = generateToken(user);
        String message = mailBuilder.build("Welcome to React-Spring-Reddit Clone. " +
                "Please visit the link below to activate you account : " + EMAIL_ACTIVATION + "/" + token);
        mailService.sendEmail(new NotificationEmail("Please Activate Your Account", user.getEmail(), message));
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        AccountVerificationToken verificationToken = new AccountVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);
        return token;
    }

    public void verifyToken (String token) {
        Optional<AccountVerificationToken> verificationToken = tokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new ActivationException("Invalid Activation Token"));
        enableAccount(verificationToken.get());
    }

    public void enableAccount(AccountVerificationToken token) {
        String username = token.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ActivationException("User not found : " + username));
        user.setAccountStatus(true);
        userRepository.save(user);
    }

}