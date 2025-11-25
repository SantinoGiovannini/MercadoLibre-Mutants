package com.mercadolibre.mutant.service;

import com.mercadolibre.mutant.dto.StatsResponse;
import com.mercadolibre.mutant.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository dnaRecordRepository;

    public StatsResponse getStats() {
        // Consultas optimizadas a la BD (Rubrica 5.2)
        long countMutant = dnaRecordRepository.countByIsMutant(true);
        long countHuman = dnaRecordRepository.countByIsMutant(false);

        double ratio = 0.0;
        if (countHuman > 0) {
            ratio = (double) countMutant / countHuman;
        } else if (countMutant > 0) {
            // Caso borde: Si hay mutantes pero 0 humanos, el ratio técnicamente es infinito.
            // Para JSON seguro, podemos devolver el número de mutantes o 1.0 según preferencia.
            // Aquí devolvemos el total de mutantes como convención de "ratio puro".
            ratio = countMutant;
        }

        return StatsResponse.builder()
                .countMutantDna(countMutant)
                .countHumanDna(countHuman)
                .ratio(ratio)
                .build();
    }
}