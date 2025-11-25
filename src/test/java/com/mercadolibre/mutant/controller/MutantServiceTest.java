package com.mercadolibre.mutant.controller;

import com.mercadolibre.mutant.entity.DnaRecord;
import com.mercadolibre.mutant.repository.DnaRecordRepository;
import com.mercadolibre.mutant.service.MutantDetector;
import com.mercadolibre.mutant.service.MutantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    @Test
    @DisplayName("Si el ADN ya existe, retorna el valor de la BD (Caché)")
    void testAnalyzeDna_Exists() {
        String[] dna = {"AAAA", "CCCC", "TTTT", "GGGG"};
        DnaRecord existingRecord = DnaRecord.builder().isMutant(true).build();

        // Simulamos que la BD ya lo tiene
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(existingRecord));

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        // Verificamos que NO se llamó al detector (ahorro de recursos)
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Si el ADN es nuevo y es mutante, se guarda en BD")
    void testAnalyzeDna_NewMutant() {
        String[] dna = {"AAAA", "CCCC", "TTTT", "GGGG"};

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        // Verificamos que se calculó y se guardó
        verify(mutantDetector).isMutant(dna);
        verify(dnaRecordRepository).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Si el ADN es nuevo y es humano, se guarda en BD")
    void testAnalyzeDna_NewHuman() {
        String[] dna = {"ATCG", "GCTA", "TACG", "CGAT"};

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(false);

        boolean result = mutantService.analyzeDna(dna);

        assertFalse(result);
        verify(dnaRecordRepository).save(any(DnaRecord.class));
    }
}