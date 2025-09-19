package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.PageResult;
import org.bytesync.hotelmanagement.dto.auth.UserDto;
import org.bytesync.hotelmanagement.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

    public PageResult<UserDto> getAll(int page, int size) {
        long count = userRepository.count();
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");
        var users = userRepository.findAllDtos(pageable);
        return new PageResult<>(users, count, page, size);
    }

    public String delete(String email) {
        var user = safeCall(userRepository.findByEmail(email), "User", email);
        userRepository.delete(user);
        return "Deleted the user " + email;
    }
}
