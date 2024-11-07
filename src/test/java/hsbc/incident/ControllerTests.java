package hsbc.incident;

import com.fasterxml.jackson.databind.ObjectMapper;
import hsbc.incident.common.Constants;
import hsbc.incident.entity.Incident;
import hsbc.incident.mapper.IncidentMapper;
import hsbc.incident.vo.IncidentVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTests {

    @Autowired
    IncidentMapper incidentMapper;

    @Autowired
    private MockMvc mockMvc;

    // create 3 incidents with id 1, 2, 3 before each test
    @BeforeEach
    public void setUp() {
        Incident incident = new Incident();
        incident.setId(1L);
        incident.setDescription("description");
        incident.setStatus("PENDING");
        incidentMapper.insert(incident);

        incident.setId(2L);
        incidentMapper.insert(incident);

        incident.setId(3L);
        incidentMapper.insert(incident);
    }

    // delete all incidents after each test
    @AfterEach
    public void tearDown() {
        incidentMapper.delete(null);
    }

    public static String toJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * create
     */
    @Test
    void whenCreateIncident_shouldSucceed() throws Exception {
        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setDescription("description");
        incidentVO.setStatus("PENDING");
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
                .andExpect(jsonPath("$.message", containsString("status must not be blank")))
                .andExpect(jsonPath("$.message", containsString("description must not be blank")));
    }

    /*
     * list
     */
    @Test
    void whenListIncident_shouldSucceed() throws Exception {
        mockMvc.perform(get("/api/incidents"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.records", hasSize(3)));
    }

    @Test
    void whenListIncidentWithPagination_shouldSucceed() throws Exception {
        mockMvc.perform(get("/api/incidents?pageNum=2&pageSize=1"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data.records", hasSize(1)));
    }

    @Test
    void whenListIncidentWithInvalidParam_shouldFail() throws Exception {
        mockMvc.perform(get("/api/incidents?pageNum=-1&pageSize=-1"))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INVALID_PARAM))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message", containsString("pageSize must be greater than or equal to 1")))
                .andExpect(jsonPath("$.message", containsString("pageNum must be greater than or equal to 1")));
    }

    /*
     * get
     */
    @Test
    void whenGetIncident_shouldSucceed() throws Exception {
        mockMvc.perform(get("/api/incidents/{id}", 1))
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
        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setDescription("modified description");
        incidentVO.setStatus("FINISHED");
        mockMvc.perform(put("/api/incidents/{id}", 1)
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void whenUpdateIncidentNotExist_shouldFail() throws Exception {
        IncidentVO incidentVO = new IncidentVO();
        incidentVO.setDescription("modified description");
        incidentVO.setStatus("FINISHED");
        mockMvc.perform(put("/api/incidents/{id}", -1)
                        .content(toJsonString(incidentVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(Constants.RESPONSE_CODE_INCIDENT_NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("incident id -1 not found"));
    }

    /*
     * delete
     */
    @Test
    void whenDeleteIncident_shouldSucceed() throws Exception {
        mockMvc.perform(delete("/api/incidents/{id}", 1))
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
