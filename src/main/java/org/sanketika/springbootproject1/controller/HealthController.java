package org.sanketika.springbootproject1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/datasetapis")
public class HealthController {
    @GetMapping("/health")
    public static ResponseEntity<Map<String,Object>> healthCheck(){
        return ResponseEntity.ok(Map.of("Status","up"));
    }
}
