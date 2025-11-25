package com.mercadolibre.mutant.dto;

import com.mercadolibre.mutant.validation.ValidDna;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DnaRequest {

    @Schema(example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]", description = "Array de Strings representando la matriz de ADN (NxN)")
    @NotNull(message = "El ADN no puede ser nulo")
    @ValidDna // ¡Aquí aplicamos nuestra validación personalizada!
    private String[] dna;
}