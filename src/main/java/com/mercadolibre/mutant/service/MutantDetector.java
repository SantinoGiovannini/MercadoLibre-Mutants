package com.mercadolibre.mutant.service;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_THRESHOLD = 1; // Más de 1 secuencia = Mutante
    // Validación de regex precompilada para rendimiento
    private static final Pattern VALID_DNA_PATTERN = Pattern.compile("^[ATCG]+$");

    public boolean isMutant(String[] dna) {
        // 1. Validaciones previas (Fail Fast)
        if (dna == null || dna.length == 0) return false;
        int n = dna.length;

        // Optimización: Conversión a char[][] para acceso O(1) (Rubrica 1.3)
        char[][] matrix = new char[n][n];

        for (int i = 0; i < n; i++) {
            if (dna[i] == null || dna[i].length() != n) return false; // Matriz debe ser NxN
            if (!VALID_DNA_PATTERN.matcher(dna[i]).matches()) return false; // Solo A,T,C,G
            matrix[i] = dna[i].toCharArray();
        }

        int sequenceCount = 0;

        // 2. Recorrido Único (Rubrica 1.4 - Single Pass)
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Buscar Horizontal
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > MUTANT_THRESHOLD) return true; // Early Termination
                    }
                }

                // Buscar Vertical
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > MUTANT_THRESHOLD) return true; // Early Termination
                    }
                }

                // Buscar Diagonal (\)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > MUTANT_THRESHOLD) return true; // Early Termination
                    }
                }

                // Buscar Diagonal (/)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > MUTANT_THRESHOLD) return true; // Early Termination
                    }
                }
            }
        }
        return false;
    }

    // Optimización: Comparación directa sin bucles internos (Rubrica 1.4)
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row][col+1] == base &&
                matrix[row][col+2] == base &&
                matrix[row][col+3] == base;
    }

    private boolean checkVertical(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row+1][col] == base &&
                matrix[row+2][col] == base &&
                matrix[row+3][col] == base;
    }

    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row+1][col+1] == base &&
                matrix[row+2][col+2] == base &&
                matrix[row+3][col+3] == base;
    }

    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row-1][col+1] == base &&
                matrix[row-2][col+2] == base &&
                matrix[row-3][col+3] == base;
    }
}