package org.bytesync.hotelmanagement.dto.room;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomGridView {

    private List<RoomDto> fourth;
    private List<RoomDto> fifth;
    private List<RoomDto> seventh;
    private List<RoomDto> eighth;

}
