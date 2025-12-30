package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.guest.GuestRecordDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.interfaces.guest.IGuestRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/guest-records/api")
public class GuestRecordsApi {

    private final IGuestRecordService guestRecordService;

    @GetMapping("/list/current")
    public ResponseEntity<ResponseMessage<PageResult<GuestRecordDto>>> getCurrentStayingGuestRecords(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        var records = guestRecordService.getAll(true, page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", records));
    }

    @GetMapping("/list/all")
    public ResponseEntity<ResponseMessage<PageResult<GuestRecordDto>>> getPreviousGuestRecords(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        var records = guestRecordService.getAll(false, page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", records));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseMessage<PageResult<GuestRecordDto>>> searchGuestRecords(@RequestParam String query,
                                                                                          @RequestParam(required = false, defaultValue = "true") boolean current,
                                                                                          @RequestParam(required = false, defaultValue = "0") int page,
                                                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        var records = guestRecordService.search(query, page, size, current);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", records));
    }
}
