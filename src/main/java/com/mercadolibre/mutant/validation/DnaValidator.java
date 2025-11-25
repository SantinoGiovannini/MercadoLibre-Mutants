package com.mercadolibre.mutant.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DnaValidator implements ConstraintValidator<ValidDna, String[]> {

    private static final Pattern VALID_CHARACTERS = Pattern.compile("^[ATCG]+$");

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length == 0) {
            return false; // No puede ser nulo ni vacío
        }

        int n = dna.length;
        for (String row : dna) {
            // Verificar nulidad y cuadratura (NxN)
            if (row == null || row.length() != n) {
                return false;
            }
            // Verificar caracteres válidos usando Regex (Rápido)
            if (!VALID_CHARACTERS.matcher(row).matches()) {
                return false;
            }
        }
        return true;
    }
}