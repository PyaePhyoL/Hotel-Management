package org.bytesync.hotelmanagement.service.hotel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.auth.*;
import org.bytesync.hotelmanagement.exception.UserAlreadyExistsException;
import org.bytesync.hotelmanagement.model.Staff;
import org.bytesync.hotelmanagement.repository.StaffRepository;
import org.bytesync.hotelmanagement.repository.specification.StaffSpecification;
import org.bytesync.hotelmanagement.security.SecurityTokenProvider;
import org.bytesync.hotelmanagement.util.mapper.StaffMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.bytesync.hotelmanagement.security.SecurityTokenProvider.Type.ACCESS;
import static org.bytesync.hotelmanagement.security.SecurityTokenProvider.Type.REFRESH;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffService {

    private final AuthenticationManager authenticationManager;
    private final StaffRepository staffRepository;
    private final SecurityTokenProvider securityTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public StaffInfo signIn(SignInForm form) {
        // authenticate the staff
        var authentication = authenticationManager.authenticate(form.authToken());
        log.info("==============> Authentication successful: {}", authentication.isAuthenticated());

        // find user in database
        var user = safeCall(staffRepository.findByEmail(form.email()), "Staff", form.email());
        // generate token
        return StaffInfo.builder()
                .userId(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .accessToken(securityTokenProvider.generate(ACCESS, authentication))
                .refreshToken(securityTokenProvider.generate(REFRESH, authentication))
                .build();
    }

    public String register(StaffRegisterForm form) {
        var staff = StaffMapper.toEntity(form);
        staff.setPassword(passwordEncoder.encode(form.password()));

        checkUserExists(staff);

        staffRepository.saveAndFlush(staff);

        return "New Staff has been created";
    }

    private void checkUserExists(Staff staff) {
        if(staffRepository.existsByEmail(staff.getEmail())) throw new UserAlreadyExistsException("Email already exists");
        if(staffRepository.existsByName(staff.getName())) throw new UserAlreadyExistsException("Name already exists");
        if(staffRepository.existsByNrc(staff.getNrc())) throw new UserAlreadyExistsException("NRC already exists");
    }

    public String update(Integer id, StaffRegisterForm form) {
        var staff = safeCall(staffRepository.findById(id), "Staff", id);
        ensureStaffUpdateNoConflict(staff, form);

        StaffMapper.update(staff, form);

        staffRepository.save(staff);
        return "Staff has been updated";
    }

    private void ensureStaffUpdateNoConflict(Staff staff, StaffRegisterForm form) {
        if(!staff.getName().equals(form.name()) && staffRepository.existsByName(form.name())) throw new UserAlreadyExistsException("Name already exists");
        if(!staff.getEmail().equals(form.email()) && staffRepository.existsByEmail(form.email())) throw new UserAlreadyExistsException("Email already exists");
        if(!staff.getNrc().equals(form.nrc()) && staffRepository.existsByNrc(form.nrc())) throw new UserAlreadyExistsException("NRC already exists");
    }

    public StaffInfo refresh(String refreshToken) {
        var authentication = securityTokenProvider.parse(REFRESH, refreshToken);
        var staff = safeCall(staffRepository.findByEmail(authentication.getName()), "Staff", authentication.getName());

        return StaffInfo.builder()
                .userId(staff.getId())
                .userName(staff.getUsername())
                .email(staff.getEmail())
                .role(staff.getRole().name())
                .accessToken(securityTokenProvider.generate(ACCESS, authentication))
                .refreshToken(securityTokenProvider.generate(REFRESH, authentication))
                .build();
    }


    public String enable(Integer id) {
        var staff = safeCall(staffRepository.findById(id), "User", id);
        staff.setEnabled(true);
        staffRepository.save(staff);
        return "Enabled the staff " + staff.getName();
    }

    public String disable(Integer id) {
        var staff = safeCall(staffRepository.findById(id), "User", id);
        staff.setEnabled(false);
        staffRepository.save(staff);
        return "Disabled the user " + staff.getName();
    }

    public PageResult<StaffDto> getAll(int page, int size) {
        long count = staffRepository.count();
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");
        var staffs = staffRepository.findAllDtos(pageable);
        return new PageResult<>(staffs, count, page, size);
    }

    public String delete(Integer id) {
        var staff = safeCall(staffRepository.findById(id), "User", id);
        staffRepository.delete(staff);
        return "Deleted the staff " + staff.getName();
    }

    public StaffDetailsDto getDetails(Integer userId) {
        return safeCall(staffRepository.findDetailsById(userId), "User", userId);
    }

    public PageResult<StaffDto> search(String query, int page, int size) {
        if(query == null || query.trim().isEmpty()) {
            return new PageResult<>(List.of(), 0, page, size);
        }

        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");
        var spec = StaffSpecification.keyword(query);

        Page<Staff> results = staffRepository.findAll(spec, pageable);
        List<StaffDto> dtos = results.stream().map(StaffMapper::toStaffDto).toList();

        return new PageResult<>(dtos, results.getTotalElements(), page, size);
    }
}
