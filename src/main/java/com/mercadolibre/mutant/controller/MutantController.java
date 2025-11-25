package com.mercadolibre.mutant.controller;

import com.mercadolibre.mutant.dto.DnaRequest;
import com.mercadolibre.mutant.dto.StatsResponse;
import com.mercadolibre.mutant.service.MutantService;
import com.mercadolibre.mutant.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping // Mapeo base
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API para detección de mutantes y estadísticas")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService; // ✅ Asegúrate que esto NO esté comentado

    @Operation(summary = "Detectar si un humano es mutante", description = "Analiza la secuencia de ADN y determina si es mutante basándose en patrones repetitivos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es Mutante"),
            @ApiResponse(responseCode = "403", description = "Es Humano (Forbidden)"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/mutant")
    public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.analyzeDna(request.getDna());
        if (isMutant) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // 👇 ESTE ES EL MÉTODO QUE FALTABA O ESTABA COMENTADO 👇
    @Operation(summary = "Obtener estadísticas", description = "Devuelve el conteo de mutantes, humanos y el ratio.")
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}