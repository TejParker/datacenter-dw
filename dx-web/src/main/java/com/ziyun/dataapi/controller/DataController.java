package com.ziyun.dataapi.controller;

import com.ziyun.dataapi.bean.DataRequest;
import com.ziyun.dataapi.bean.DataResponse;
import com.ziyun.dataapi.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {


    private final DataService dataService;
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }


    @PostMapping
    public ResponseEntity<?> retrieveData(@RequestBody DataRequest request) {
        try {
            DataResponse response = dataService.processRequest(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        }
    }
}
