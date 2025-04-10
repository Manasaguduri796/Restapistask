package org.sanketika.springbootproject1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void health_check_should_be_getChecked () throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/datasetapis/health")//sending get request using mock mvc
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        System.out.println("Status"+ status);

        assertEquals(200,status);
        String result = mvcResult.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(result, Map.class);

        assertEquals("up", response.get("Status"));



    }

}
