package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.GuestRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guest-records/api")
public class GuestRecordsApi {

    private final GuestRecordService guestRecordService;

    @GetMapping("/list/current")
    public ResponseEntity<ResponseMessage> getCurrentStayingGuestRecords(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        var records = guestRecordService.getAll(true, page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Current Guest Records", records));
    }

    @GetMapping("/list/all")
    public ResponseEntity<ResponseMessage> getAllGuestRecords(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        var records = guestRecordService.getAll(false, page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "All Guest Records", records));
    }
}
