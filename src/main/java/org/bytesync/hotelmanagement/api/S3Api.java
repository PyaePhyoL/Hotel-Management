package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.s3.S3PutRequestDto;
import org.bytesync.hotelmanagement.s3.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Api {

    private final S3Service s3Service;

    @PostMapping("/put-presigned-url")
    public ResponseEntity<ResponseMessage<String>> getPutPreSignedUrl(@RequestBody S3PutRequestDto dto){
        var url = s3Service.createPutPreSignedUrl(dto);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                url
        ));
    }

}
