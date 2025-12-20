package org.bytesync.hotelmanagement.service.interfaces.guest;

import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.enums.GuestStatus;

public interface IGuestService {

    String register(GuestDto form);

    GuestDto getDetails(Long id);

    PageResult<GuestDto> getAll(int page, int size);

    String update(Long id, GuestDto form);

    String delete(Long id);

    PageResult<GuestDto> search(String query, int page, int size);

    String deleteRelation(Long rsId);

    String changeStatus(Long id, GuestStatus status);
}
