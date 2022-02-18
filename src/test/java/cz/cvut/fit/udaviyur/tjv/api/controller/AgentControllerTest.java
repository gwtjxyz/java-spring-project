package cz.cvut.fit.udaviyur.tjv.api.controller;

import cz.cvut.fit.udaviyur.tjv.domain.Agent;
import cz.cvut.fit.udaviyur.tjv.service.AgentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AgentController.class)
class AgentControllerTest {

    @MockBean
    AgentService agentService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllAgents() throws Exception {
        Agent agent1 = new Agent("QWE", "ASD", "ZXC");
        Agent agent2 = new Agent("123", "AAA", "BBB");
        List<Agent> agents = List.of(agent1, agent2);

        when(agentService.getAllAgents()).thenReturn(agents);

        mockMvc.perform(get("/api/agents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("QWE")))
                .andExpect(jsonPath("$[0].country", Matchers.is("ASD")))
                .andExpect(jsonPath("$[0].nickname", Matchers.is("ZXC")))
                .andExpect(jsonPath("$[1].name", Matchers.is("123")))
                .andExpect(jsonPath("$[1].country", Matchers.is("AAA")))
                .andExpect(jsonPath("$[1].nickname", Matchers.is("BBB")));
    }

    @Test
    void getAgentById() throws Exception {
        Agent agent1 = new Agent("QWE", "ASD", "ZXC");

        when(agentService.getAgentById(1L)).thenReturn(Optional.of(agent1));

        mockMvc.perform(get("/api/agents/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("QWE")))
                .andExpect(jsonPath("$.country", Matchers.is("ASD")))
                .andExpect(jsonPath("$.nickname", Matchers.is("ZXC")));
    }

    @Test
    void getAgentByNickname() throws Exception {
        Agent agent1 = new Agent("QWE", "ASD", "ZXC");

        when(agentService.getAgentByNickname("ZXC")).thenReturn(Optional.of(agent1));

        mockMvc.perform(get("/api/agents/nickname/ZXC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("QWE")))
                .andExpect(jsonPath("$.country", Matchers.is("ASD")))
                .andExpect(jsonPath("$.nickname", Matchers.is("ZXC")));
    }

    @Test
    void getAgentsByName() throws Exception {
        Agent agent1 = new Agent("QWE", "ASD", "ZXC");
        Agent agent2 = new Agent("QWE", "GGG", "ZXCVBN");
        List<Agent> agents = List.of(agent1, agent2);

        when(agentService.getAgentsByName("QWE")).thenReturn(agents);

        mockMvc.perform(get("/api/agents/name/QWE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("QWE")))
                .andExpect(jsonPath("$[0].country", Matchers.is("ASD")))
                .andExpect(jsonPath("$[0].nickname", Matchers.is("ZXC")))
                .andExpect(jsonPath("$[1].name", Matchers.is("QWE")))
                .andExpect(jsonPath("$[1].country", Matchers.is("GGG")))
                .andExpect(jsonPath("$[1].nickname", Matchers.is("ZXCVBN")));
    }

    @Test
    void getAgentsByCountry() throws Exception {
        Agent agent1 = new Agent("QWE", "ASD", "ZXC");
        Agent agent2 = new Agent("123", "ASD", "ZXCVBN");
        List<Agent> agents = List.of(agent1, agent2);

        when(agentService.getAgentsByCountry("ASD")).thenReturn(agents);

        mockMvc.perform(get("/api/agents/country/ASD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("QWE")))
                .andExpect(jsonPath("$[0].country", Matchers.is("ASD")))
                .andExpect(jsonPath("$[0].nickname", Matchers.is("ZXC")))
                .andExpect(jsonPath("$[1].name", Matchers.is("123")))
                .andExpect(jsonPath("$[1].country", Matchers.is("ASD")))
                .andExpect(jsonPath("$[1].nickname", Matchers.is("ZXCVBN")));
    }

    @Test
    void addNewAgent() throws Exception {
        mockMvc.perform(post("/api/agents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"QWE\",\"country\":\"ASD\",\"nickname\":\"ZXC\"}"))
                .andExpect(status().isOk());

        verify(agentService, times(1)).addNewAgent(new Agent("QWE", "ASD", "ZXC"));
    }

    @Test
    void deleteAgent() throws Exception {
        mockMvc.perform(delete("/api/agents/id/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/agents/nickname/abc"))
                .andExpect(status().isOk());

        verify(agentService, times(1)).deleteAgent(1L);
        verify(agentService, times(1)).deleteAgent("abc");
    }

    @Test
    void updateAgent() throws Exception {
        Agent agent1 = new Agent("QWE", "ASD", "ZXC");

        mockMvc.perform(put("/api/agents/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"QWE\",\"country\":\"ASD\",\"nickname\":\"ZXC\"}"))
                .andExpect(status().isOk());

        verify(agentService, times(1)).updateAgent(1L, agent1);
    }

    @Test
    void assignContractToAgent() throws Exception {
        mockMvc.perform(put("/api/agents/1/assign/1"))
                .andExpect(status().isOk());

        verify(agentService, times(1)).assignContractToAgent(1L, 1L);
    }
}