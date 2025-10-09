package org.bytesync.hotelmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytesync.hotelmanagement.dto.PageResult;
import org.bytesync.hotelmanagement.dto.auth.*;
import org.bytesync.hotelmanagement.exception.UserAlreadyExistsException;
import org.bytesync.hotelmanagement.model.User;
import org.bytesync.hotelmanagement.repository.UserRepository;
import org.bytesync.hotelmanagement.security.SecurityTokenProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.bytesync.hotelmanagement.security.SecurityTokenProvider.Type.ACCESS;
import static org.bytesync.hotelmanagement.security.SecurityTokenProvider.Type.REFRESH;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

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
        var user = User.builder()
                .name(form.name())
                .email(form.email())
                .password(passwordEncoder.encode(form.password()))
                .role(form.role())
                .position(form.position())
                .nrc(form.nrc())
                .birthDate(form.birthDate())
                .joinDate(form.joinDate())
                .address(form.address())
                .enabled(true)
                .build();
        checkUserExists(user);

        userRepository.saveAndFlush(user);

        return "New User has been created";
    }

    private void checkUserExists(User user) {
        if(userRepository.existsByEmail(user.getEmail())) throw new UserAlreadyExistsException("Email already exists");
        if(userRepository.existsByName(user.getName())) throw new UserAlreadyExistsException("Name already exists");
        if(userRepository.existsByNrc(user.getNrc())) throw new UserAlreadyExistsException("NRC already exists");
    }

    public String update(Long id, RegisterForm form) {
        var user = safeCall(userRepository.findById(id), "User", id);
        ensureUserUpdateNoConflict(user, form);

        user.setName(form.name());
        user.setEmail(form.email());
        user.setRole(form.role());
        user.setPosition(form.position());
        user.setNrc(form.nrc());
        user.setBirthDate(form.birthDate());
        user.setJoinDate(form.joinDate());
        user.setAddress(form.address());

        userRepository.save(user);
        return "User has been updated";
    }

    private void ensureUserUpdateNoConflict(User user, RegisterForm form) {
        if(!user.getName().equals(form.name()) && userRepository.existsByName(form.name())) throw new UserAlreadyExistsException("Name already exists");
        if(!user.getEmail().equals(form.email()) && userRepository.existsByEmail(form.email())) throw new UserAlreadyExistsException("Email already exists");
        if(!user.getNrc().equals(form.nrc()) && userRepository.existsByNrc(form.nrc())) throw new UserAlreadyExistsException("NRC already exists");
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


    public String enable(Long id) {
        var user = safeCall(userRepository.findById(id), "User", id);
        user.setEnabled(true);
        userRepository.save(user);
        return "Enabled the user " + user.getName();
    }

    public String disable(Long id) {
        var user = safeCall(userRepository.findById(id), "User", id);
        user.setEnabled(false);
        userRepository.save(user);
        return "Disabled the user " + user.getName();
    }

    public PageResult<UserDto> getAll(int page, int size) {
        long count = userRepository.count();
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");
        var users = userRepository.findAllDtos(pageable);
        return new PageResult<>(users, count, page, size);
    }

    public String delete(Long id) {
        var user = safeCall(userRepository.findById(id), "User", id);
        userRepository.delete(user);
        return "Deleted the user " + user.getName();
    }

    public UserDetailsDto getDetails(Long userId) {
        return safeCall(userRepository.findDetailsById(userId), "User", userId);
    }
}
