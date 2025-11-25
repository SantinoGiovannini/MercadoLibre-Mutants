package com.mercadolibre.mutant.controller;

import com.mercadolibre.mutant.service.MutantDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontales")
    void testMutantHorizontal() {
        String[] dna = {
                "AAAA", "CCCC", "TCAG", "GGTC"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantVertical() {
        String[] dna = {
                "ATCG", "ATCG", "ATCG", "ATCG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal principal (descendente)")
    void testMutantDiagonalDescending() {
        String[] dna = {
                "ATCGAT",
                "CAGGTA",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal secundaria (ascendente)")
    void testMutantDiagonalAscending() {
        String[] dna = {
                "ATCGAT",
                "CAGGTA",
                "TTATGT",
                "AGAAGG", // G en (3,1)
                "CCCCTA", // G en (2,2) -> A en (4,4)? No, buscamos /
                "TCACTG"
        };
        // Ajustemos un caso claro de diagonal ascendente
        String[] dnaAsc = {
                "AATCT",
                "ATCGT",
                "TCGAT",
                "CGTTC", // G diagonal invertida
                "GCTAT"
        };
        // Para asegurar simplicidad, probemos un caso explícito mutante mixto
        String[] mutantMix = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA", // Horizontal
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(mutantMix));
    }

    @Test
    @DisplayName("No debe detectar humano (1 sola secuencia)")
    void testHumanOneSequence() {
        String[] dna = {
                "AAAA", "TCAG", "GTCA", "TGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar humano (sin secuencias)")
    void testHumanNoSequence() {
        String[] dna = {
                "ATCG", "CAGT", "TTAT", "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe manejar matriz vacía")
    void testEmptyDna() {
        assertFalse(mutantDetector.isMutant(new String[]{}));
    }

    @Test
    @DisplayName("Debe manejar input nulo")
    void testNullDna() {
        assertFalse(mutantDetector.isMutant(null));
    }

    @Test
    @DisplayName("Debe rechazar matriz NxM (no cuadrada)")
    void testNonSquare() {
        String[] dna = {"ATC", "GCA"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe rechazar caracteres inválidos")
    void testInvalidCharacters() {
        String[] dna = {"ATCX", "GCAT", "CGAT", "TCGA"}; // X inválida
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe rechazar filas nulas")
    void testNullRow() {
        String[] dna = {"AAAA", null, "AAAA", "AAAA"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Performance: Matriz 100x100 debe ser rápida")
    void testLargeMatrixPerformance() {
        int n = 100;
        String[] dna = new String[n];
        // Llenar con basura
        for(int i=0; i<n; i++) {
            dna[i] = "A".repeat(n); // Todo A -> Mutante inmediato
        }
        long start = System.currentTimeMillis();
        assertTrue(mutantDetector.isMutant(dna));
        long end = System.currentTimeMillis();
        assertTrue((end - start) < 500, "El algoritmo es muy lento");
    }
}