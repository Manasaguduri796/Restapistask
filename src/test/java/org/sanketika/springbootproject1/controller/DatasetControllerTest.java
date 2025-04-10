package org.sanketika.springbootproject1.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.sanketika.springbootproject1.repository.DatasetRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;



import org.sanketika.springbootproject1.entity.Dataset;
import org.sanketika.springbootproject1.entity.Status;
import org.sanketika.springbootproject1.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.MvcResult;


import java.io.DataInput;
import java.time.LocalDateTime;
import java.util.*;

import static org.apache.coyote.http11.Constants.a;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//TEST READY  purose is  to ensure we are running mockito class nothing else we are to test methods in side not web servers
@SpringBootTest
@TestPropertySource("classpath:application.properties")
@AutoConfigureMockMvc
class DatasetControllerTest {
    @Autowired
    private MockMvc mockMvc; // this object  handles the http request and validates responses
    @Autowired
    private ObjectMapper objectMapper; // FOR converting java object to json  we use object mapper object her

    @Mock
    private DatasetRepository datasetRepository;


    private Object requestBody;
    private Optional<Dataset> dataset;
    private Dataset Dataset;




    @Test
    void getDatasetsAll_shouldReturnGetAll() throws Exception {
       Dataset dataset = new Dataset();
        dataset.setCreatedBy("system");
        dataset.setUpdatedBy("system");
        dataset.setCreatedByDate(LocalDateTime.now());
        dataset.setUpdatedByDate(LocalDateTime.now());

        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("key", "value");
        dataset.setRouterConfig(routerConfig);

        List<Dataset> datasetList = Collections.singletonList(dataset);

        when(datasetRepository.findAll()).thenReturn(datasetList);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getAll")
                        .content(objectMapper.writeValueAsString(datasetList))).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);


        String result = mvcResult.getResponse().getContentAsString();

           Map<String, Object> resposeObject = objectMapper.readValue(result, Map.class);
           List<Dataset> result1 = (List<Dataset>) resposeObject.get("result");

           Map<String,Object> response = (Map<String, Object>) result1.get(0);

assertNotNull(datasetList);
assertEquals("system",response.get("createdBy"));



        }

        @Test
    void getById_ReturnSuccessfully() throws Exception {
        Dataset dataset = new Dataset();
        dataset.setId("1");
        Map<String, Object> dataSchema = new HashMap<>();
        dataSchema.put("json", "random");
        dataset.setDataSchema(dataSchema);
        Map<String,Object> routerConfig = new HashMap<>();
        routerConfig.put("json","random");
        dataset.setCreatedBy("system");
        dataset.setUpdatedBy("system");
        dataset.setCreatedByDate(LocalDateTime.now());
        dataset.setUpdatedByDate(LocalDateTime.now());
        when(datasetRepository.findById("1")).thenReturn(Optional.of(dataset));
        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = response.getContentAsString();
        Map<String, Object> result = objectMapper.readValue(content, Map.class);
        Map<String, Object> result1 = (Map<String, Object>) result.get("result");
        assertEquals("1", result1.get("id"));
        assertEquals("system", result1.get("createdBy"));
        assertEquals("SYSTEM",result1.get("updatedBy"));
        assertNotNull(dataSchema);
        assertNotNull(routerConfig);

    }

    @Test
    void getByStatus_should_return_successfully() throws Exception {
        Dataset datasets = new Dataset();
        datasets.setId("1");
        datasets.setCreatedBy("system");
        datasets.setUpdatedBy("system");
        datasets.setStatus(Status.DRAFT);
        datasets.setCreatedByDate(LocalDateTime.now());
        datasets.setUpdatedByDate(LocalDateTime.now());

        List<Dataset> datasetList = Collections.singletonList(datasets);

        when(datasetRepository.findByStatus(Status.DRAFT)).thenReturn(datasetList);

        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getByStatus?status=DRAFT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) responseMap.get("result");
        Map<String, Object> result = resultList.get(0);

        assertEquals("DRAFT", result.get("status"));
        assertEquals("system", result.get("createdBy"));
    }

    @Test
    void createDataset_shouldReturnCreatedDataset() throws Exception {
        Dataset dataset1 = new Dataset();
        dataset1.setId("1");
        dataset1.setCreatedBy("SYSTEM");
        dataset1.setUpdatedBy("SYSTEM");
        dataset1.setCreatedByDate(LocalDateTime.parse("2025-03-20T11:37:58.927496"));
        dataset1.setUpdatedByDate(LocalDateTime.parse("2025-03-20T11:37:58.927496"));
        dataset1.setDataSchema(new HashMap<>());
        dataset1.setRouterConfig(new HashMap<>());

        when(datasetRepository.save(any(Dataset.class))).thenReturn(dataset1);

        MvcResult mvcResult = mockMvc.perform(post("/datasetapis/create")
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();

        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
        assertNotNull(responseMap.get("id"));

        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        if (result != null) {
            assertEquals(201, status);
            assertEquals("SYSTEM", result.get("createdBy"));
            assertEquals("SYSTEM", result.get("updatedBy"));
            assertNotNull(result.get("createdByDate"));
            assertNotNull(result.get("updatedByDate"));
            assertNotNull(result.get("dataSchema"));
            assertNotNull(result.get("routerConfig"));
        } else {
            System.out.println("key is not found");
        }
    }



    @Test
void updatedDataset_should_ReturnSuccessfully_with_ok() throws Exception {
        Dataset dataset1 = new Dataset();
        dataset1.setId("1");
        dataset1.setUpdatedBy("system");
        dataset1.setUpdatedBy("system");
        dataset1.setStatus(Status.DRAFT);
        dataset1.setCreatedByDate(LocalDateTime.now());
        dataset1.setUpdatedByDate(LocalDateTime.now());
        Map<String,Object> dataSchema =  new HashMap<>();
        dataSchema.put("json","random");
        dataset1.setDataSchema(dataSchema);
        Map<String,Object> routerConfig =  new HashMap<>();
        routerConfig.put("json","random");
        dataset1.setRouterConfig(routerConfig);

        when(datasetRepository.save(any(Dataset.class))).thenReturn(dataset1);
        when(datasetRepository.existsById("1")).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(put("/datasetapis/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dataset1))).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        String response = mvcResult.getResponse().getContentAsString();
        System.out.println("response"+response);
        Map<String,Dataset> responseMap = objectMapper.readValue(response, Map.class);

        Map<String,Object> result = (Map<String, Object>) responseMap.get("result");
        assertNotNull(dataSchema);
        assertNotNull(routerConfig);
        assertEquals("1",result.get("id"));


    }












}









