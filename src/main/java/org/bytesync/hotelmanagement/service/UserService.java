package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.auth.*;
import org.bytesync.hotelmanagement.exception.UserAlreadyExistsException;
import org.bytesync.hotelmanagement.model.User;
import org.bytesync.hotelmanagement.repository.UserRepository;
import org.bytesync.hotelmanagement.security.SecurityTokenProvider;
import org.bytesync.hotelmanagement.util.mapper.UserMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        var user = UserMapper.toEntity(form);
        user.setPassword(passwordEncoder.encode(form.password()));

        checkUserExists(user);

        userRepository.saveAndFlush(user);

        return "New User has been created";
    }

    private void checkUserExists(User user) {
        if(userRepository.existsByEmail(user.getEmail())) throw new UserAlreadyExistsException("Email already exists");
        if(userRepository.existsByName(user.getName())) throw new UserAlreadyExistsException("Name already exists");
        if(userRepository.existsByNrc(user.getNrc())) throw new UserAlreadyExistsException("NRC already exists");
    }

    public String update(Integer id, RegisterForm form) {
        var user = safeCall(userRepository.findById(id), "User", id);
        ensureUserUpdateNoConflict(user, form);

        UserMapper.update(user, form);

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


    public String enable(Integer id) {
        var user = safeCall(userRepository.findById(id), "User", id);
        user.setEnabled(true);
        userRepository.save(user);
        return "Enabled the user " + user.getName();
    }

    public String disable(Integer id) {
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

    public String delete(Integer id) {
        var user = safeCall(userRepository.findById(id), "User", id);
        userRepository.delete(user);
        return "Deleted the user " + user.getName();
    }

    public UserDetailsDto getDetails(Integer userId) {
        return safeCall(userRepository.findDetailsById(userId), "User", userId);
    }
}
