package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.GuestDto;
import org.bytesync.hotelmanagement.dto.auth.UserDto;
import org.bytesync.hotelmanagement.exception.UserAlreadyExistsException;
import org.bytesync.hotelmanagement.model.Guest;
import org.bytesync.hotelmanagement.repository.GuestRepository;
import org.bytesync.hotelmanagement.util.mapper.GuestMapper;
import org.springframework.stereotype.Service;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;


    public String register(GuestDto form) {
        Guest guest = GuestMapper.toEntity(form);

        checkGuestExists(guest);

        guestRepository.save(guest);

        return "New Guest has been created";
    }

    private void checkGuestExists(Guest guest) {
        if(guestRepository.existsByEmail(guest.getEmail())) throw new UserAlreadyExistsException("Email already exists");
        if(guestRepository.existsByNationalId(guest.getNationalId())) throw new UserAlreadyExistsException("National Id already exists");
        if(guestRepository.existsByPassport(guest.getPassport())) throw new UserAlreadyExistsException("Passport already exists");
        if(guestRepository.existsByPhone(guest.getPhone())) throw new UserAlreadyExistsException("Phone already exists");
    }

    public GuestDto getDetails(int id) {
        return safeCall(guestRepository.findGuestDtoById(id), "Guest", id);
    }
}
