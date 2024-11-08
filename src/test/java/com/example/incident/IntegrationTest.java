package com.example.incident;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.example.incident.common.Constants;
import com.example.incident.vo.IncidentVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.incident.Utils.toJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * Integration Testing
     */
    @Test
    void whenPerformIntegrationTestAllApi_shouldSucceed() throws Exception {
        // verify there are no incidents
        mockMvc.perform(get("/api/incidents"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.records", hasSize(0)));

        // create an incident
        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setReporter("Thomas");
        incidentVO.setTitle("an incident");
        incidentVO.setDescription("description");
        incidentVO.setStatus("Pending");
        incidentVO.setPriority("Low");
        MvcResult result = mockMvc.perform(post("/api/incidents")
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").isNumber())
                .andReturn();
        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.data");

        // get the created incident
        mockMvc.perform(get("/api/incidents/{id}", id))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").isMap());

        // verify there are 1 incident
        mockMvc.perform(get("/api/incidents"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.records", hasSize(1)));

        // update the incident
        incidentVO.setDescription("modified description");
        incidentVO.setStatus("Resolved");
        mockMvc.perform(put("/api/incidents/{id}", id)
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").value(true));

        // verify the update
        mockMvc.perform(get("/api/incidents/{id}", id))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.description").value("modified description"))
                .andExpect(jsonPath("$.data.status").value("Resolved"));

        // delete the incident
        mockMvc.perform(delete("/api/incidents/{id}", id))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").value(true));

        // verify the deletion
        mockMvc.perform(get("/api/incidents/{id}", id))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INCIDENT_NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(String.format("incident id %s not found", id)));

        // verify there are no incidents
        mockMvc.perform(get("/api/incidents"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.records", hasSize(0)));
    }
}
