package br.com.fiap.msclientes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setEmail("email@cliente.teste");
    }

    @Test
    void testFindAll_ReturnsOk() throws Exception {
        List<Cliente> clientes = List.of(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(cliente.getNome()));
    }

    @Test
    void testFindAll_ReturnsNoContent() throws Exception {
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_ReturnsOk() throws Exception {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/{id}", cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(cliente.getNome()));
    }

    @Test
    void testFindById_ReturnsNoContent() throws Exception {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/{id}", cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testInsert_ReturnsCreated() throws Exception {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(cliente.getNome()));
    }

    @Test
    void testUpdate_ReturnsCreated() throws Exception {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/clientes/{id}", cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(cliente.getNome()));
    }

    @Test
    void testUpdate_ReturnsBadRequest() throws Exception {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.empty());

        mockMvc.perform(put("/clientes/{id}", cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDelete_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/clientes/{id}", cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
