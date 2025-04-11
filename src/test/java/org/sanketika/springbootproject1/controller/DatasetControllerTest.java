package org.sanketika.springbootproject1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.sanketika.springbootproject1.repository.DatasetRepository;
import org.sanketika.springbootproject1.entity.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.sanketika.springbootproject1.entity.Status.DRAFT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



//TEST READY  purose is  to ensure we are running mockito class nothing else we are to test methods in side not web servers
@SpringBootTest
@TestPropertySource("classpath:application.properties")
@AutoConfigureMockMvc
class DatasetControllerTest {
    @Autowired
    private MockMvc mockMvc; // this object  handles the http request and validates responses

    @Autowired
    private ObjectMapper objectMapper; // FOR converting java object to json  we use object mapper object her

    @MockitoBean
    private DatasetRepository datasetRepository;


    private Object requestBody;


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
        assertEquals(200, status);


        String result = mvcResult.getResponse().getContentAsString();

        Map<String, Object> resposeObject = objectMapper.readValue(result, Map.class);
        List<Dataset> result1 = (List<Dataset>) resposeObject.get("result");

        Map<String, Object> response = (Map<String, Object>) result1.get(0);

        assertNotNull(datasetList);
        assertEquals("system", response.get("createdBy"));


    }

    @Test
    void getById_ReturnSuccessfully() throws Exception {
        Dataset dataset = new Dataset();
        dataset.setId("1");
        Map<String, Object> dataSchema = new HashMap<>();
        dataSchema.put("json", "random");
        dataset.setDataSchema(dataSchema);
        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("json", "random");
        dataset.setCreatedBy("system");
        dataset.setUpdatedBy("system");
        dataset.setCreatedByDate(LocalDateTime.now());
        dataset.setUpdatedByDate(LocalDateTime.now());
        when(datasetRepository.findById("1")).thenReturn(Optional.of(dataset));
        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Map<String, Object> result = objectMapper.readValue(content, Map.class);
        Map<String, Object> result1 = (Map<String, Object>) result.get("result");
        assertEquals("1", result1.get("id"));
        assertEquals("system", result1.get("createdBy"));
        assertNotNull(dataSchema);
        assertNotNull(routerConfig);

    }

    @Test
    void getByStatus_should_return_successfully() throws Exception {
        Dataset datasets = new Dataset();
        datasets.setId("1");
        datasets.setCreatedBy("system");
        datasets.setUpdatedBy("system");
        datasets.setStatus(DRAFT);
        datasets.setCreatedByDate(LocalDateTime.now());
        datasets.setUpdatedByDate(LocalDateTime.now());

        List<Dataset> datasetList = Collections.singletonList(datasets);

        when(datasetRepository.findByStatus(DRAFT)).thenReturn(datasetList);

        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getByStatus?status=DRAFT")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
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

        Map<String, Object> dataSchema = new HashMap<>();
        dataSchema.put("json", "random");
        dataset1.setDataSchema(dataSchema);
        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("json", "random");
        dataset1.setRouterConfig(routerConfig);
        when(datasetRepository.save(any(Dataset.class))).thenReturn(dataset1);

        MvcResult mvcResult = mockMvc.perform(post("/datasetapis/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dataset1)))
                        .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
        assertNotNull(responseMap.get("id"));

        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        assertEquals(201, status);

        assertEquals("1", result.get("id"));
        assertEquals("Dataset saved successfully with ID: 1", result.get("message"));
    }


    @Test
    void updatedDataset_should_ReturnSuccessfully_with_Not() throws Exception {
        Dataset dataset1 = new Dataset();
        dataset1.setId("1");
        dataset1.setUpdatedBy("system");
        dataset1.setUpdatedBy("system");
        dataset1.setStatus(DRAFT);
        dataset1.setCreatedByDate(LocalDateTime.now());
        dataset1.setUpdatedByDate(LocalDateTime.now());
        Map<String, Object> dataSchema = new HashMap<>();
        dataSchema.put("json", "random");
        dataset1.setDataSchema(dataSchema);
        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("json", "random");
        dataset1.setRouterConfig(routerConfig);

        when(datasetRepository.save(any(Dataset.class))).thenReturn(dataset1);
        when(datasetRepository.existsById("1")).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(put("/datasetapis/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dataset1))).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
        String response = mvcResult.getResponse().getContentAsString();
        System.out.println("response" + response);
        Map<String, Dataset> responseMap = objectMapper.readValue(response, Map.class);

        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        assertNotNull(dataSchema);
        assertNotNull(routerConfig);


    }


    @Test
    void deleteById_Should_Return_Successfully() throws Exception {

        Dataset dataset = new Dataset();
        dataset.setUpdatedBy("system");
        dataset.setUpdatedBy("system");
        dataset.setStatus(DRAFT);
        dataset.setCreatedByDate(LocalDateTime.now());
        dataset.setUpdatedByDate(LocalDateTime.now());
        Map<String, Object> dataSchema = new HashMap<>();
        dataSchema.put("json", "random");
        dataset.setDataSchema(dataSchema);
        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("json", "random");
        dataset.setRouterConfig(routerConfig);
        when(datasetRepository.findById("1")).thenReturn(Optional.of(dataset));
        doNothing().when(datasetRepository).deleteById("1");
        MvcResult mvcResult = mockMvc.perform(delete("/datasetapis/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);


    }

    @Test
    void getByAll_Should_return_ok_When_Empty_List() throws Exception {
        Dataset dataset = new Dataset();
        dataset.setCreatedBy("system");
        dataset.setUpdatedBy("system");
        dataset.setCreatedByDate(LocalDateTime.now());
        dataset.setUpdatedByDate(LocalDateTime.now());

        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("key", "value");
        dataset.setRouterConfig(routerConfig);
        List<Dataset> datasetList = Collections.emptyList();

        when(datasetRepository.findAll()).thenReturn(datasetList);
        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void get_by_id_should_return_notFound_scenario() throws Exception {

        Dataset dataset = new Dataset();
        dataset.setId("1");
        Map<String, Object> dataSchema = new HashMap<>();
        dataSchema.put("json", "random");
        dataset.setDataSchema(dataSchema);
        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("json", "random");
        dataset.setCreatedBy("system");
        dataset.setUpdatedBy("system");
        dataset.setCreatedByDate(LocalDateTime.now());
        dataset.setUpdatedByDate(LocalDateTime.now());
        when(datasetRepository.findById("1")).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    void deleteById_Should_Return_Not_Found() throws Exception {
        Dataset dataset = new Dataset();
        dataset.setUpdatedBy("system");
        dataset.setUpdatedBy("system");
        dataset.setStatus(DRAFT);
        dataset.setCreatedByDate(LocalDateTime.now());
        dataset.setUpdatedByDate(LocalDateTime.now());
        Map<String, Object> dataSchema = new HashMap<>();
        dataSchema.put("json", "random");
        dataset.setDataSchema(dataSchema);
        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("json", "random");
        dataset.setRouterConfig(routerConfig);
        doNothing().when(datasetRepository).deleteById("1");
        MvcResult mvcResult = mockMvc.perform(delete("/datasetapis/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
        String content = mvcResult.getResponse().getContentAsString();
        Map<String, Object> result = objectMapper.readValue(content, Map.class);
        Map<String, Object> result1 = (Map<String, Object>) result.get("result");

    }

    @Test
    void getByStatus_should_Return_Bad_Request_when_Status_empty() throws Exception {

        Dataset datasets = new Dataset();
        datasets.setId("1");
        datasets.setCreatedBy("system");
        datasets.setUpdatedBy("system");
        datasets.setStatus(DRAFT);
        datasets.setCreatedByDate(LocalDateTime.now());
        datasets.setUpdatedByDate(LocalDateTime.now());

        List<Dataset> datasetList = Collections.singletonList(datasets);

        when(datasetRepository.findByStatus(DRAFT)).thenReturn(datasetList);


        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getByStatus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

    }

    @Test
    void getByStatus_should_return_bad_request_when_status_is_invalid() throws Exception {
        Dataset datasets = new Dataset();
        datasets.setId("1");
        datasets.setCreatedBy("system");
        datasets.setUpdatedBy("system");
        datasets.setStatus(DRAFT);
        datasets.setCreatedByDate(LocalDateTime.now());
        datasets.setUpdatedByDate(LocalDateTime.now());
        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getByStatus?status=INVALID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);


    }

    @Test
    void createDataset_shouldReturnBadRequest_whenMissingRequiredFields() throws Exception {
        Dataset invalidRequest = new Dataset();


        MvcResult mvcResult = mockMvc.perform(post("/datasetapis/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println("Bad Request Response: " + responseBody);
    }


    @Test
    void updateDataset_shouldReturnUpdatedDataset_whenValidRequest() throws Exception {
        Dataset existingDataset = new Dataset();
        existingDataset.setId("1");
        existingDataset.setCreatedBy("system");
        existingDataset.setUpdatedBy("system");
        existingDataset.setCreatedByDate(LocalDateTime.now());
        existingDataset.setUpdatedByDate(LocalDateTime.now());
        existingDataset.setDataSchema(Map.of("json", "random "));
        existingDataset.setRouterConfig(Map.of("json", "random"));
        existingDataset.setStatus(DRAFT);


        Dataset updatedDataset = new Dataset();
        updatedDataset.setId("1");
        updatedDataset.setCreatedBy("system");
        updatedDataset.setUpdatedBy("system");
        updatedDataset.setCreatedByDate(LocalDateTime.now());
        updatedDataset.setUpdatedByDate(LocalDateTime.now());
        updatedDataset.setDataSchema(Map.of("json", "random"));
        updatedDataset.setRouterConfig(Map.of("json", "random"));
        updatedDataset.setStatus(DRAFT);

        when(datasetRepository.findById("1")).thenReturn(Optional.of(existingDataset));
        when(datasetRepository.save(any(Dataset.class))).thenReturn(updatedDataset);

        MvcResult mvcResult = mockMvc.perform(put("/datasetapis/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDataset)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());

        assertEquals(200, status);

        Map<String, Object> responseMap = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        assertEquals("1", result.get("id"));
        assertEquals("Dataset updated successfully with ID: 1", result.get("message"));

    }

    @Test
    void updateDataset_shouldReturnUpdatedDataset_when_badRequest() throws Exception {
        Dataset existingDataset = new Dataset();
        Dataset updatedDataset = new Dataset();
        when(datasetRepository.findById("1")).thenReturn(Optional.of(existingDataset));
        when(datasetRepository.save(any(Dataset.class))).thenReturn(updatedDataset);

        MvcResult mvcResult = mockMvc.perform(put("/datasetapis/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingDataset)))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }

    @Test
    void createDataset_shouldReturnConflict_whenDatasetAlreadyExists() throws Exception {
        Dataset dataset = new Dataset();
        dataset.setId("1");
        dataset.setUpdatedBy("system");
        dataset.setCreatedBy("system");
        dataset.setCreatedByDate(LocalDateTime.now());
        dataset.setUpdatedByDate(LocalDateTime.now());

        Map<String, Object> dataSchema = new HashMap<>();
        dataSchema.put("json", "random");
        dataset.setDataSchema(dataSchema);

        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("json", "random");
        dataset.setRouterConfig(routerConfig);
        when(datasetRepository.existsById("1")).thenReturn(true);
        when(datasetRepository.save(any(Dataset.class))).thenReturn(dataset);

        MvcResult mvcResult = mockMvc.perform(post("/datasetapis/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dataset)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(409, status);

    }


    @Test
    void createDataset_shouldReturn_whenUnexpectedErrorOccurs() throws Exception {
        Dataset dataset = new Dataset();
        dataset.setId("1");
        dataset.setCreatedBy("system");
        dataset.setCreatedByDate(LocalDateTime.now());
        dataset.setUpdatedBy("system");
        dataset.setUpdatedByDate(LocalDateTime.now());
        Map<String, Object> dataSchema = new HashMap<>();
        dataSchema.put("json", "random");
        dataset.setDataSchema(dataSchema);
        Map<String, Object> routerConfig = new HashMap<>();
        routerConfig.put("json", "random");
        dataset.setRouterConfig(routerConfig);

        when(datasetRepository.existsById("1")).thenReturn(false);
        when(datasetRepository.save(any(Dataset.class))).thenThrow(new RuntimeException());

        MvcResult mvcResult = mockMvc.perform(post("/datasetapis/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dataset)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(500, status);


    }


    @Test
    void updateDataset_shouldReturnUpdatedDataset_internal_Server_error() throws Exception {
        Dataset existingDataset = new Dataset();
        existingDataset.setId("1");
        existingDataset.setCreatedBy("system");
        existingDataset.setUpdatedBy("system");
        existingDataset.setCreatedByDate(LocalDateTime.now());
        existingDataset.setUpdatedByDate(LocalDateTime.now());
        existingDataset.setDataSchema(Map.of("json", "random "));
        existingDataset.setRouterConfig(Map.of("json", "random"));
        existingDataset.setStatus(DRAFT);


        Dataset updatedDataset = new Dataset();
        updatedDataset.setId("1");
        updatedDataset.setCreatedBy("system");
        updatedDataset.setUpdatedBy("system");
        updatedDataset.setCreatedByDate(LocalDateTime.now());
        updatedDataset.setUpdatedByDate(LocalDateTime.now());
        updatedDataset.setDataSchema(Map.of("json", "random"));
        updatedDataset.setRouterConfig(Map.of("json", "random"));
        updatedDataset.setStatus(DRAFT);

        when(datasetRepository.findById("1")).thenReturn(Optional.of(existingDataset));
        when(datasetRepository.save(any(Dataset.class))).thenThrow(new RuntimeException());

        MvcResult mvcResult = mockMvc.perform(put("/datasetapis/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDataset)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();


        assertEquals(500, status);


    }

    @Test
    void shouldHandleJDBCConnectionException() throws Exception {
        when(datasetRepository.findById("1"))
                .thenThrow(new JDBCConnectionException("DB down", new SQLException()));

        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getById/1")).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);

    }

    @Test
    void shouldHandleNoResourceFoundException() throws Exception {
        Dataset dataset = new Dataset();
        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getById/1/").contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
        String response = mvcResult.getResponse().getContentAsString();
        Map<String, Object> error_msg = objectMapper.readValue(response, Map.class);
        Map<String, Object> params = (Map<String, Object>) error_msg.get("params");
        assertEquals("requested dataset id is not found or route is incorrect", params.get("error_msg"));
    }

//    @Test
//    void shouldHandleMethodArgumentException() throws Exception{
//        Dataset dataset = new Dataset();
//        Map<String,Object> dataSchema = new HashMap<>();
//        dataSchema.put("json","normal");
//        dataset.setDataSchema(null);
//        MvcResult mvcResult = mockMvc.perform(post("/datasetapis/create").contentType(MediaType.APPLICATION_JSON)).andReturn();
//        int status =mvcResult.getResponse().getStatus();
//        assertEquals(400,status);
//        String response = mvcResult.getResponse().getContentAsString();
//        Map<String, Object> error_msg = objectMapper.readValue(response, Map.class);
//        Map<String, Object> params = (Map<String, Object>) error_msg.get("params");
//        assertEquals("invalid request parameter is provided",params.get("error_msg"));
//}

//    @Test
//    void shouldReturnErrorWhenSchemaIsNull() throws Exception {
//        Dataset validator = new Dataset();
//        Map<String,Object> dataSchema = new HashMap<>();
//        dataSchema.put("json","random");
//        validator.setDataSchema(dataSchema);
//        MvcResult mvcResult = mockMvc.perform(post("/datasetapis/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(validator))).andReturn();
//
//        Map<String, Object> result = validator.getDataSchema();
//        String response = mvcResult.getResponse().getContentAsString();
//
//
//        assertEquals("dataSchema is required", result.get("dataSchema"));
//    }
@Test
void shouldHandleMethodArgumentException() throws Exception {
    String invalidJson = "{}";

    MvcResult mvcResult = mockMvc.perform(post("/datasetapis/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
            .andReturn();

    int status = mvcResult.getResponse().getStatus();
    assertEquals(400, status);

    String response = mvcResult.getResponse().getContentAsString();
    Map<String, Object> errorMsg = objectMapper.readValue(response, Map.class);

}
    @Test
    void shouldHandleGenericException() throws Exception {
        Dataset dataset = new Dataset();

        MvcResult result = mockMvc.perform(get("/datasetapis/create"))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(500, status);

        String response = result.getResponse().getContentAsString();
        Map<String, Object> errorMsg = objectMapper.readValue(response, Map.class);

        Map<String, Object> params = (Map<String, Object>) errorMsg.get("params");
        assertEquals("An unexpected error is occured", params.get("error_msg"));
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/getById/abc"))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        String content = mvcResult.getResponse().getContentAsString();
        Map<String, Object> body = objectMapper.readValue(content, Map.class);
        assertEquals("invalid request parameter is provided.", body.get("message"));
    }

}









