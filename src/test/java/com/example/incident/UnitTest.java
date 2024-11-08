package com.example.incident;

import com.example.incident.common.Constants;
import com.example.incident.mapper.IncidentMapper;
import com.example.incident.service.IncidentService;
import com.example.incident.vo.IncidentVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.incident.Utils.toJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UnitTest {

    @Autowired
    IncidentService incidentService;

    @Autowired
    IncidentMapper incidentMapper;

    @Autowired
    private MockMvc mockMvc;

    // delete all incidents after each test
    @AfterEach
    public void tearDown() {
        incidentMapper.delete(null);
    }

    public Long createIncident() {
        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setReporter("Thomas");
        incidentVO.setTitle("an incident");
        incidentVO.setDescription("description");
        incidentVO.setStatus("Pending");
        incidentVO.setPriority("Low");
        return incidentService.create(incidentVO);
    }

    /*
     * create
     */
    @Test
    void whenCreateIncident_shouldSucceed() throws Exception {
        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setReporter("Thomas");
        incidentVO.setTitle("an incident");
        incidentVO.setDescription("description");
        incidentVO.setStatus("Pending");
        incidentVO.setPriority("Low");
        mockMvc.perform(post("/api/incidents")
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").isNumber());
    }

    @Test
    void whenCreateIncidentMissingParam_shouldFail() throws Exception {
        IncidentVO incidentVO = new IncidentVO();
        mockMvc.perform(post("/api/incidents")
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INVALID_PARAM))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message", containsString("reporter must not be blank")))
                .andExpect(jsonPath("$.message", containsString("title must not be blank")))
                .andExpect(jsonPath("$.message", containsString("description must not be blank")))
                .andExpect(jsonPath("$.message", containsString("status must not be blank")))
                .andExpect(jsonPath("$.message", containsString("priority must not be blank")));
    }

    @Test
    void whenCreateIncidentInvalidParam_shouldFail() throws Exception {
        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setReporter("Thomas");
        incidentVO.setTitle("an incident");
        incidentVO.setDescription("description");
        incidentVO.setStatus("unknown");
        incidentVO.setPriority("unknown");
        mockMvc.perform(post("/api/incidents")
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INVALID_PARAM))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message", containsString("status must match \"Pending|Processing|Resolved\"")))
                .andExpect(jsonPath("$.message", containsString("priority must match \"Low|Medium|High\"")));
    }

    /*
     * list
     */
    @Test
    void whenListIncident_shouldSucceed() throws Exception {
        createIncident();
        mockMvc.perform(get("/api/incidents"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.records", hasSize(1)));
    }

    @Test
    void whenListIncidentWithPagination_shouldSucceed() throws Exception {
        createIncident();
        createIncident();
        mockMvc.perform(get("/api/incidents?pageNum=2&pageSize=1"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.records", hasSize(1)));
    }

    @Test
    void whenListIncidentWithFilter_shouldSucceed() throws Exception {
        createIncident();
        mockMvc.perform(get("/api/incidents?status=Pending"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.records", hasSize(1)));
        mockMvc.perform(get("/api/incidents?status=Resolved"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.records", hasSize(0)));
    }

    @Test
    void whenListIncidentWithInvalidParam_shouldFail() throws Exception {
        mockMvc.perform(get("/api/incidents?pageNum=-1&pageSize=-1"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INVALID_PARAM))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message", containsString("pageSize must be between 1 and 100")))
                .andExpect(jsonPath("$.message", containsString("pageNum must be greater than or equal to 1")));
    }

    /*
     * get
     */
    @Test
    void whenGetIncident_shouldSucceed() throws Exception {
        Long id = createIncident();
        mockMvc.perform(get("/api/incidents/{id}", id))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").isMap());
    }

    @Test
    void whenGetIncidentNotExist_shouldFail() throws Exception {
        mockMvc.perform(get("/api/incidents/{id}", -1))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INCIDENT_NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("incident id -1 not found"));
    }

    /*
     * update
     */
    @Test
    void whenUpdateIncident_shouldSucceed() throws Exception {
        Long id = createIncident();

        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setDescription("modified description");
        incidentVO.setStatus("Pending");
        mockMvc.perform(put("/api/incidents/{id}", id)
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void whenUpdateIncidentNotExist_shouldFail() throws Exception {
        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setDescription("modified description");
        incidentVO.setStatus("Pending");
        mockMvc.perform(put("/api/incidents/{id}", -1)
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INCIDENT_NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("incident id -1 not found"));
    }

    @Test
    void whenUpdateIncidentInvalidParam_shouldFail() throws Exception {
        Long id = createIncident();

        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setStatus("unknown");
        incidentVO.setPriority("unknown");
        mockMvc.perform(put("/api/incidents/{id}", id)
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INVALID_PARAM))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message", containsString("status must match \"Pending|Processing|Resolved\"")))
                .andExpect(jsonPath("$.message", containsString("priority must match \"Low|Medium|High\"")));
    }

    /*
     * delete
     */
    @Test
    void whenDeleteIncident_shouldSucceed() throws Exception {
        Long id = createIncident();
        mockMvc.perform(delete("/api/incidents/{id}", id))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void whenDeleteIncidentNotExist_shouldFail() throws Exception {
        mockMvc.perform(delete("/api/incidents/{id}", -1))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INCIDENT_NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("incident id -1 not found"));
    }
}
