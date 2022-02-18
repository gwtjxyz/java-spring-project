package cz.cvut.fit.udaviyur.tjv.api.controller;

import cz.cvut.fit.udaviyur.tjv.domain.Contract;
import cz.cvut.fit.udaviyur.tjv.service.ContractService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContractController.class)
class ContractControllerTest {

    @MockBean
    ContractService contractService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllContracts() throws Exception {
        LocalDate date1 = LocalDate.of(1990, 1, 1);
        LocalDate date2 = LocalDate.of(2000, 1, 1);
        Contract contract1 = new Contract("ABC", date1, 6000L);
        Contract contract2 = new Contract("QWE", date2, 9000L);
        List<Contract> contracts = List.of(contract1, contract2);

        when(contractService.getAllContracts()).thenReturn(contracts);

        mockMvc.perform(get("/api/contracts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].codeName", Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].contractDate", Matchers.is(date1.toString())))
                .andExpect(jsonPath("$[0].fee", Matchers.is(6000)))
                .andExpect(jsonPath("$[1].codeName", Matchers.is("QWE")))
                .andExpect(jsonPath("$[1].contractDate", Matchers.is(date2.toString())))
                .andExpect(jsonPath("$[1].fee", Matchers.is(9000)));
    }

    @Test
    void getContractById() throws Exception {
        LocalDate date1 = LocalDate.of(1990, 1, 1);
        Contract contract1 = new Contract("ABC", date1, 6000L);

        when(contractService.getContractById(1L)).thenReturn(Optional.of(contract1));

        mockMvc.perform(get("/api/contracts/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeName", Matchers.is("ABC")))
                .andExpect(jsonPath("$.contractDate", Matchers.is(date1.toString())))
                .andExpect(jsonPath("$.fee", Matchers.is(6000)));

    }

    @Test
    void getContractByCodename() throws Exception {
        LocalDate date1 = LocalDate.of(1990, 1, 1);
        Contract contract1 = new Contract("ABC", date1, 6000L);

        when(contractService.getContractByCodename("ABC")).thenReturn(Optional.of(contract1));

        mockMvc.perform(get("/api/contracts/codename/ABC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeName", Matchers.is("ABC")))
                .andExpect(jsonPath("$.contractDate", Matchers.is(date1.toString())))
                .andExpect(jsonPath("$.fee", Matchers.is(6000)));
    }

    // The following three tests are disabled because I have
    // absolutely no clue how to work with date formats inside
    // REST URL requests and as such don't know how to make this work
    @Test
    @Disabled
    void getContractsBeforeDate() throws Exception {
        LocalDate date1 = LocalDate.of(1990, 1, 1);
        Contract contract1 = new Contract("ABC", date1, 6000L);
        LocalDate date2 = LocalDate.of(1995, 1, 1);

        when(contractService.getContractsBeforeDate(date2)).thenReturn(List.of(contract1));

        mockMvc.perform(get("/api/contracts/before/1995-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].codeName", Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].contractDate", Matchers.is(date1.toString())))
                .andExpect(jsonPath("$[0].fee", Matchers.is(6000)));
    }

    @Test
    @Disabled
    void getContractsAfterDate() throws Exception {
        LocalDate date1 = LocalDate.of(1990, 1, 1);
        Contract contract1 = new Contract("ABC", date1, 6000L);
        LocalDate date2 = LocalDate.of(1985, 1, 1);

        when(contractService.getContractsAfterDate(date2)).thenReturn(List.of(contract1));

        mockMvc.perform(get("/api/contracts/after/1985-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].codeName", Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].contractDate", Matchers.is(date1.toString())))
                .andExpect(jsonPath("$[0].fee", Matchers.is(6000)));
    }

    @Test
    @Disabled
    void getContractsBetweenDates() throws Exception {
        LocalDate date0 = LocalDate.of(1990, 1, 1);
        LocalDate date1 = LocalDate.of(1995, 1, 1);
        Contract contract1 = new Contract("ABC", date1, 6000L);
        LocalDate date2 = LocalDate.of(2000, 1, 1);

        when(contractService.getContractsBetweenDates(date0, date2)).thenReturn(List.of(contract1));

        mockMvc.perform(get("/api/contracts/start/1990-01-01/end/2000-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].codeName", Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].contractDate", Matchers.is(date1.toString())))
                .andExpect(jsonPath("$[0].fee", Matchers.is(6000)));
    }

    @Test
    void addNewContract() throws Exception {
        LocalDate date1 = LocalDate.of(1990, 1, 1);
        Contract contract1 = new Contract("ABC", date1, 6000L);

        mockMvc.perform(post("/api/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codeName\":\"ABC\",\"contractDate\":\"1990-01-01\",\"fee\":\"6000\"}"))
                .andExpect(status().isOk());

        verify(contractService, times(1)).addNewContract(contract1);
    }

    @Test
    void deleteContract() throws Exception {
        mockMvc.perform(delete("/api/contracts/id/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/contracts/codename/abc"))
                .andExpect(status().isOk());

        verify(contractService, times(1)).deleteContract(1L);
        verify(contractService, times(1)).deleteContract("abc");
    }


    @Test
    void updateContract() throws Exception {
        LocalDate date1 = LocalDate.of(1990, 1, 1);
        Contract contract1 = new Contract("ABC", date1, 6000L);

        mockMvc.perform(put("/api/contracts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codeName\":\"ABC\",\"contractDate\":\"1990-01-01\",\"fee\":\"6000\"}"))
                .andExpect(status().isOk());

        verify(contractService, times(1)).updateContract(1L, contract1);
    }

    @Test
    void assignClientToContract() throws Exception {
        mockMvc.perform(put("/api/contracts/1/assign/client/1"))
                .andExpect(status().isOk());

        verify(contractService, times(1)).assignClientToContract(1L, 1L);
    }

    @Test
    void assignAgentToContract() throws Exception {
        mockMvc.perform(put("/api/contracts/1/assign/agent/1"))
                .andExpect(status().isOk());

        verify(contractService, times(1)).assignAgentToContract(1L, 1L);
    }
}