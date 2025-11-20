package org.bytesync.hotelmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.exception.UserAlreadyExistsException;
import org.bytesync.hotelmanagement.model.Guest;
import org.bytesync.hotelmanagement.repository.GuestRepository;
import org.bytesync.hotelmanagement.repository.ContactRepository;
import org.bytesync.hotelmanagement.repository.specification.GuestSpecification;
import org.bytesync.hotelmanagement.util.mapper.GuestMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final ContactRepository contactRepository;


    public String register(GuestDto form) {
        Guest guest = GuestMapper.toEntity(form);

        checkGuestExists(guest);

        guestRepository.save(guest);

        return "New Guest has been created";
    }

    private void checkGuestExists(Guest guest) {
        if(guestRepository.existsByEmail(guest.getEmail())) throw new UserAlreadyExistsException("Email already exists");
        if(guestRepository.existsByNrc(guest.getNrc())) throw new UserAlreadyExistsException("National Id already exists");
        if(null != guest.getPassport() && guestRepository.existsByPassport(guest.getPassport())) throw new UserAlreadyExistsException("Passport already exists");
    }

    public GuestDto getDetails(int id) {
        var guest = safeCall(guestRepository.findById(id), "Guest", id);
        return GuestMapper.toDto(guest);
    }

    public PageResult<GuestDto> getAll(int page, int size) {
        long count = guestRepository.count();
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");
        List<GuestDto> guestList = guestRepository.findAllGuestDto(pageable);

        return new PageResult<>(guestList, count, page, size);
    }

    public String update(int id, GuestDto form) {
        var guest = guestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        ensureGuestUpdateNoConflict(guest, form);
        GuestMapper.updateGuest(guest, form);
        guestRepository.save(guest);

        return "Guest has been updated";
    }

    private void ensureGuestUpdateNoConflict(Guest guest, GuestDto form) {
        if(!guest.getName().equals(form.getName()) && guestRepository.existsByName(form.getName())) throw new UserAlreadyExistsException("Name already exists");
        if(!guest.getEmail().equals(form.getEmail()) && guestRepository.existsByEmail(form.getEmail())) throw new UserAlreadyExistsException("Email already exists");
        if(!guest.getNrc().equals(form.getNrc()) && guestRepository.existsByNrc(form.getNrc())) throw new UserAlreadyExistsException("NRC already exists");
        if(null != guest.getPassport() && !guest.getPassport().equals(form.getPassport()) && guestRepository.existsByPassport(form.getPassport())) throw new UserAlreadyExistsException("Passport already exists");
    }

    public String delete(int id) {
        var guest = safeCall(guestRepository.findById(id), "Guest", id);
        guestRepository.delete(guest);
        return "Guest has been deleted";
    }

    public PageResult<GuestDto> search(String query, int page, int size) {
        if(query == null || query.trim().isEmpty()) {
            return new PageResult<>(List.of(), 0, page, size);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        var specification = GuestSpecification.keyword(query.trim());

        Page<Guest> result = guestRepository.findAll(specification, pageable);

        List<GuestDto> dtos = result.stream().map(GuestMapper::toDto).toList();

        return new PageResult<>(dtos, result.getTotalElements(), page, size);
    }

    public String deleteRelation(int rsId) {
        var relation = safeCall(contactRepository.findById(rsId), "Relation", rsId);
        contactRepository.delete(relation);
        return "Relation has been deleted";
    }
}

