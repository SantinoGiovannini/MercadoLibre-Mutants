package com.mercadolibre.mutant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.mutant.dto.DnaRequest;
import com.mercadolibre.mutant.dto.StatsResponse;
import com.mercadolibre.mutant.service.MutantService;
import com.mercadolibre.mutant.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean; <-- ESTE ERA EL VIEJO
import org.springframework.test.context.bean.override.mockito.MockitoBean; // <-- ESTE ES EL NUEVO DE SPRING BOOT 3.4
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // En Spring Boot 3.4, @MockBean se cambia por @MockitoBean
    @MockitoBean
    private MutantService mutantService;

    @MockitoBean
    private StatsService statsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /mutant retorna 200 OK si es mutante")
    void testIsMutant_200() throws Exception {
        DnaRequest request = new DnaRequest();
        request.setDna(new String[]{"AAAA", "CCCC", "TTTT", "GGGG"});

        when(mutantService.analyzeDna(any())).thenReturn(true);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant retorna 403 Forbidden si es humano")
    void testIsMutant_403() throws Exception {
        DnaRequest request = new DnaRequest();
        request.setDna(new String[]{"ATCG", "GCTA", "TACG", "CGAT"});

        when(mutantService.analyzeDna(any())).thenReturn(false);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant retorna 400 Bad Request si el ADN es inválido")
    void testIsMutant_400() throws Exception {
        String jsonInvalid = "{\"dna\": null}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalid))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /stats retorna JSON correcto")
    void testGetStats() throws Exception {
        StatsResponse mockResponse = StatsResponse.builder()
                .countMutantDna(40)
                .countHumanDna(100)
                .ratio(0.4)
                .build();

        when(statsService.getStats()).thenReturn(mockResponse);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }
}