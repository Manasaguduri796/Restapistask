package org.sanketika.springbootproject1.controller;
import org.sanketika.springbootproject1.entity.Dataset;
import org.sanketika.springbootproject1.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@ControllerAdvice
@Validated
@RequestMapping("/datasetapis")
public class DatasetController {
    @Autowired
    private DatasetService datasetService;

    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getDatasetAll() {
        return datasetService.getDatasetAll();
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<Map<String, Object>> getDatasetById(@PathVariable String id) {
        return datasetService.getById(id);
    }


    @GetMapping("/getByStatus")
    public ResponseEntity<?> getByDatasetByStatus(@RequestParam(name = "status", required = false) String status) {
        return datasetService.getByStatus(status);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String,Object>> CreateDataset(@RequestBody String datasetJson) {
        return datasetService.createDataset(datasetJson);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> UpdateDatasetById(@PathVariable String id, @RequestBody String updateDataset) {
        return datasetService.updateDatasetById(id, updateDataset);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDatasetById(@PathVariable String id) {
        return datasetService.deleteDatasetById(id);
    }



}






