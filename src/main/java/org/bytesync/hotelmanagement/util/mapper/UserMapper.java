package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.auth.RegisterForm;
import org.bytesync.hotelmanagement.dto.auth.UserDto;
import org.bytesync.hotelmanagement.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(RegisterForm form) {
        return User.builder()
                .name(form.name())
                .email(form.email())
                .role(form.role())
                .position(form.position())
                .nrc(form.nrc())
                .birthDate(form.birthDate())
                .joinDate(form.joinDate())
                .address(form.address())
                .enabled(true)
                .build();
    }

    public static void update(User user, RegisterForm form) {
        user.setName(form.name());
        user.setEmail(form.email());
        user.setRole(form.role());
        user.setPosition(form.position());
        user.setNrc(form.nrc());
        user.setBirthDate(form.birthDate());
        user.setJoinDate(form.joinDate());
        user.setAddress(form.address());

    }
}
