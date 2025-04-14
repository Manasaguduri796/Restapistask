package org.sanketika.springbootproject1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sanketika.springbootproject1.entity.Dataset;
import org.sanketika.springbootproject1.entity.Status;
import org.sanketika.springbootproject1.repository.DatasetRepository;
import org.sanketika.springbootproject1.response.DatasetResponse;
import org.sanketika.springbootproject1.response.ResponsePost;
import org.sanketika.springbootproject1.response.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service

public class DatasetService {
    @Autowired
    private final DatasetRepository datasetRepository;
    private Object SimpleResponse;
    @Autowired
    private ObjectMapper objectMapper;
    private Dataset updatedDataset;


    public DatasetService(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    //GETALL
    public ResponseEntity<?> getDatasetAll() {

        List<Dataset> datasetList = datasetRepository.findAll();
        if (datasetList.isEmpty()) {
            return (ResponseEntity.ok(DatasetResponse.createResponse("Success", HttpStatus.OK, "no dataset found", Collections.emptyList())));
        }
        return (ResponseEntity.ok(DatasetResponse.createResponse("Success", HttpStatus.OK, null, datasetList)));
    }

    //GETBYID
    public ResponseEntity<Map<String,Object>> getById(int id) {

        String idString = new String(String.valueOf(id));
        Optional<Dataset> dataset = datasetRepository.findById(idString);
        if (dataset.isPresent()) {
            return (ResponseEntity.ok(DatasetResponse.createResponse("Success", HttpStatus.OK, null, datasetRepository.findById(idString))));
        }
        else {
            return (ResponseEntity.status(HttpStatus.NOT_FOUND).body(DatasetResponse.createResponse("Failure", HttpStatus.NOT_FOUND, "Requested dataset id is not found", null)));
        }


    }
//GETBYSTATUS

    public ResponseEntity<?> getByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return ResponseEntity.badRequest().body( DatasetResponse.createResponse("failure", HttpStatus.BAD_REQUEST, "status parameter is required", null));
        }
        try {
            Status enumStatus = Status.valueOf(status.toUpperCase());
            List<Dataset> datasetList = datasetRepository.findByStatus(enumStatus);
            return ResponseEntity.ok( DatasetResponse.createResponse(
                    "success", HttpStatus.OK, null, datasetList));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body( DatasetResponse.createResponse(
                    "failure", HttpStatus.BAD_REQUEST, "Invalid status. Allowed: LIVE, DRAFT, RETIRED", null));
        }

    }

    //Create
    @Transactional
    public ResponseEntity<Map<String,Object>> createDataset(String datasetJson) {
        try {
            Dataset dataset = objectMapper.readValue(datasetJson, Dataset.class);
            Optional<String> validateError = Validation.validate(dataset);
            if (validateError.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DatasetResponse.createResponse("Fail", HttpStatus.BAD_REQUEST, validateError.get(), null));
            }
            if (datasetRepository.existsById(dataset.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(DatasetResponse.createResponse("Fail", HttpStatus.CONFLICT, "Requested id is already existed", null));
            }
            dataset.setStatus(Status.valueOf("DRAFT"));
            dataset.setUpdatedBy("SYSTEM");
            dataset.setCreatedByDate(LocalDateTime.now());
            dataset.setUpdatedByDate(LocalDateTime.now());

            Dataset savedDataset = datasetRepository.save(dataset);
            SimpleResponse simpleResponse = new SimpleResponse(savedDataset.getId(),
                    "Dataset saved successfully with ID: " + savedDataset.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(DatasetResponse.createResponse("Success", HttpStatus.CREATED, "null", simpleResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DatasetResponse.createResponse("Fail", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }

    //updated dataset by id
     public ResponseEntity<Map<String,Object>> updateDatasetById(String id, String updateDataset) {
        try {

            Dataset updateData = objectMapper.readValue(updateDataset,Dataset.class);
            Optional<String> validationError = Validation.validateForUpdate(updateData);
            if(validationError.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DatasetResponse.createResponse("fail",HttpStatus.BAD_REQUEST,validationError.get(),null));
            }
            Optional<Dataset> datasetExi = datasetRepository.findById(id);
            if (datasetExi.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DatasetResponse.createResponse("Fail", HttpStatus.NOT_FOUND, "requested dataset id not found ", null));
            }
            Dataset existingDataset = datasetExi.get();

            existingDataset.setUpdatedBy("SYSTEM");
            existingDataset.setUpdatedByDate(LocalDateTime.now());

            if (updateData.getDataSchema() != null) {
                existingDataset.setDataSchema(updateData.getDataSchema());
            }
            if (updateData.getRouterConfig() != null) {
                existingDataset.setRouterConfig(updateData.getRouterConfig());
            }
            if (updateData.getStatus() != null) {
                existingDataset.setStatus(updateData.getStatus());
            }


            Dataset updateDatasets = datasetRepository.save(existingDataset);
            SimpleResponse simpleResponse = new SimpleResponse(existingDataset.getId(),"Dataset updated successfully with ID: " + existingDataset.getId());
            return ResponseEntity.ok().body(DatasetResponse.createResponse("Success", HttpStatus.OK, "null", simpleResponse));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DatasetResponse.createResponse("fail", HttpStatus.INTERNAL_SERVER_ERROR, "An error is occured", null));
        }
    }

    //DELETEBYID
    public ResponseEntity<Map<String,Object>> deleteDatasetById(String id) {
        Optional<Dataset> datasetOpt = datasetRepository.findById(id);
        if (!datasetOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    DatasetResponse.createResponse("Fail", HttpStatus.NOT_FOUND, "Dataset not found", null));
        }
        datasetRepository.deleteById(id);
        return ResponseEntity.ok(ResponsePost.createResponses("Success", HttpStatus.OK, "Dataset id is deleted successfully", null));

    }

}








