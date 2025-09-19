package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytesync.hotelmanagement.dto.auth.RegisterForm;
import org.bytesync.hotelmanagement.dto.auth.UserInfo;
import org.bytesync.hotelmanagement.dto.auth.SignInForm;
import org.bytesync.hotelmanagement.exception.UserAlreadyExistsException;
import org.bytesync.hotelmanagement.model.User;
import org.bytesync.hotelmanagement.repository.UserRepository;
import org.bytesync.hotelmanagement.security.SecurityTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.bytesync.hotelmanagement.security.SecurityTokenProvider.Type.ACCESS;
import static org.bytesync.hotelmanagement.security.SecurityTokenProvider.Type.REFRESH;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SecurityTokenProvider securityTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserInfo signIn(SignInForm form) {
        // authenticate the user
        var authentication = authenticationManager.authenticate(form.authToken());

        log.info("==============> Authentication successful: {}", authentication.isAuthenticated());

        // find user in database
        var user = safeCall(userRepository.findByEmail(form.email()), "User", form.email());
        // generate token
        return UserInfo.builder()
                .userId(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .accessToken(securityTokenProvider.generate(ACCESS, authentication))
                .refreshToken(securityTokenProvider.generate(REFRESH, authentication))
                .build();
    }

    public String register(RegisterForm form) {

        if(userRepository.existsByEmail(form.email()))
            throw new UserAlreadyExistsException("Email already exists");

        var user = User.builder()
                .username(form.username())
                .email(form.email())
                .password(passwordEncoder.encode(form.password()))
                .role(form.role())
                .enabled(true)
                .build();
        userRepository.saveAndFlush(user);
        return "New User has been created";
    }

    public UserInfo refresh(String refreshToken) {
        var authentication = securityTokenProvider.parse(REFRESH, refreshToken);
        var user = safeCall(userRepository.findByEmail(authentication.getName()), "User", authentication.getName());

        return UserInfo.builder()
                .userId(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .accessToken(securityTokenProvider.generate(ACCESS, authentication))
                .refreshToken(securityTokenProvider.generate(REFRESH, authentication))
                .build();
    }

    public String enable(String email) {
        var user = safeCall(userRepository.findByEmail(email), "User", email);
        user.setEnabled(true);
        userRepository.save(user);
        return "Enabled the user " + email;
    }

    public String disable(String email) {
        var user = safeCall(userRepository.findByEmail(email), "User", email);
        user.setEnabled(false);
        userRepository.save(user);
        return "Disabled the user " + email;
    }
}
