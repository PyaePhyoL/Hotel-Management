package org.bytesync.hotelmanagement.service.interfaces.hotel;

import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.room.RoomDashboardView;
import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.dto.room.RoomOverviewDetails;
import org.bytesync.hotelmanagement.dto.room.RoomSelectList;
import org.bytesync.hotelmanagement.enums.RoomStatus;

import java.util.List;

public interface IRoomService {

    RoomDashboardView getAllRoomsInGridView(RoomStatus roomStatus);

    PageResult<RoomDto> getList(int page, int size);

    RoomOverviewDetails getRoomOverviewDetailsById(Long id);

    List<RoomSelectList> selectAvailableRoomList();

    List<RoomSelectList> selectAvailableDoubleRoomList();

    String changeRoomStatus(Long id, RoomStatus status);

    String changeRoomPrice(Long id, Integer price);

    List<RoomDto> getAllAvailableRooms();

}
