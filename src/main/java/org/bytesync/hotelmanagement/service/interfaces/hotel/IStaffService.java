package org.bytesync.hotelmanagement.service.interfaces.hotel;

import org.bytesync.hotelmanagement.dto.auth.*;
import org.bytesync.hotelmanagement.dto.output.PageResult;

public interface IStaffService {

    StaffInfo signIn(SignInForm form);

    String register(StaffRegisterForm form);

    String update(Long id, StaffRegisterForm form);

    StaffInfo refresh(String refreshToken);

    String enable(Long id);

    String disable(Long id);

    PageResult<StaffDto> getAll(int page, int size);

    String delete(Long id);

    StaffDetailsDto getDetails(Long userId);

    PageResult<StaffDto> search(String query, int page, int size);

    String changePassword(Long id, ChangePasswordDto dto);
}
