package com.example.incident;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.example.incident.common.Constants;
import com.example.incident.vo.IncidentVO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class StressTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void performStressTest() {
        // first the logic of testAPI() is correct
        testAPI();

        // then perform the stress test
        int concurrency = 2000;
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        for (int i = 0; i < concurrency; i++) {
            executor.submit(this::testAPI);
        }
        executor.shutdown();
    }

    @SneakyThrows
    private void testAPI() {
        // create
        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setDescription("description");
        incidentVO.setStatus("PENDING");
        MvcResult result = mockMvc.perform(post("/api/incidents")
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Long id = JsonPath.read(result.getResponse().getContentAsString(), "$.data");

        // get
        mockMvc.perform(get("/api/incidents/{id}", id))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").isMap());

        // list
        mockMvc.perform(get("/api/incidents"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS));

        // update
        incidentVO.setDescription("modified description");
        incidentVO.setStatus("FINISHED");
        mockMvc.perform(put("/api/incidents/{id}", id)
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").value(true));

        // delete
        mockMvc.perform(delete("/api/incidents/{id}", id))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").value(true));
    }

    public static String toJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}