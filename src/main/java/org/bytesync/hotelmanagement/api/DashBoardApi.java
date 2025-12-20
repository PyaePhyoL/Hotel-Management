package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.MainDashboard;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.service.impl.DashboardService;
import org.bytesync.hotelmanagement.service.interfaces.IDashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard/api")
public class DashBoardApi {

    private final IDashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ResponseMessage<MainDashboard>> dashboard(@RequestParam(required = false) RoomStatus status) {
        var dashboard = dashboardService.getDashboardData(status);
        return ResponseEntity.ok().body(new ResponseMessage<>(HttpStatus.OK.value(), "", dashboard));
    }
}
