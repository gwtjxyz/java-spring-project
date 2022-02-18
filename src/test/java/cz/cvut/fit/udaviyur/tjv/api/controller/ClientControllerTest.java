package cz.cvut.fit.udaviyur.tjv.api.controller;

import cz.cvut.fit.udaviyur.tjv.domain.Client;
import cz.cvut.fit.udaviyur.tjv.service.ClientService;
import org.hamcrest.Matchers;
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
@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @MockBean
    ClientService clientService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllClients() throws Exception {
        Client client1 = new Client("John Doe", "USA");
        Client client2 = new Client("Jane Doe", "USA");
        List<Client> clients = List.of(client1, client2);

        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("John Doe")))
                .andExpect(jsonPath("$[0].country", Matchers.is("USA")))
                .andExpect(jsonPath("$[1].name", Matchers.is("Jane Doe")))
                .andExpect(jsonPath("$[1].country", Matchers.is("USA")));
    }

    @Test
    void getClient() throws Exception {
        Client client1 = new Client("John Doe", "USA");


        when(clientService.getClientById(1L)).thenReturn(Optional.of(client1));

        mockMvc.perform(get("/api/clients/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("John Doe")))
                .andExpect(jsonPath("$.country", Matchers.is("USA")));
    }

    @Test
    void getClientsByName() throws Exception {
        Client client1 = new Client("John Doe", "USA");
        Client client2 = new Client("John Doe", "Zimbabwe");
        List<Client> clients = List.of(client1, client2);

        when(clientService.getClientsByName("John Doe")).thenReturn(clients);

        mockMvc.perform(get("/api/clients/name/John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("John Doe")))
                .andExpect(jsonPath("$[0].country", Matchers.is("USA")))
                .andExpect(jsonPath("$[1].name", Matchers.is("John Doe")))
                .andExpect(jsonPath("$[1].country", Matchers.is("Zimbabwe")));
    }

    @Test
    void getClientsByCountry() throws Exception  {
        Client client1 = new Client("John Doe", "USA");
        Client client2 = new Client("Jane Doe", "USA");
        List<Client> clients = List.of(client1, client2);

        when(clientService.getClientsByCountry("USA")).thenReturn(clients);

        mockMvc.perform(get("/api/clients/country/USA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("John Doe")))
                .andExpect(jsonPath("$[0].country", Matchers.is("USA")))
                .andExpect(jsonPath("$[1].name", Matchers.is("Jane Doe")))
                .andExpect(jsonPath("$[1].country", Matchers.is("USA")));
    }

    @Test
    void addNewClient() throws Exception {
        mockMvc.perform(post("/api/clients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"John Doe\",\"country\":\"USA\"}"))
                .andExpect(status().isOk());

        verify(clientService, times(1)).addNewClient(new Client("John Doe", "USA"));
    }

    @Test
    void deleteClient() throws Exception {
        mockMvc.perform(delete("/api/clients/id/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/clients/info/John Doe/USA"))
                .andExpect(status().isOk());

        verify(clientService, times(1)).deleteClient(1L);
        verify(clientService, times(1)).deleteClient("John Doe", "USA");
    }

    @Test
    void updateClient() throws Exception {
        Client client1 = new Client("John Doe", "USA");

        mockMvc.perform(put("/api/clients/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"John Doe\",\"country\":\"USA\"}"))
                .andExpect(status().isOk());

        verify(clientService, times(1)).updateClient(1L, client1);
    }

    @Test
    void assignContractToClient() throws Exception {
        mockMvc.perform(put("/api/clients/1/assign/1"))
                .andExpect(status().isOk());

        verify(clientService, times(1)).assignContractToClient(1L, 1L);
    }
}