package com.mercadolibre.mutant.service;

import com.mercadolibre.mutant.entity.DnaRecord;
import com.mercadolibre.mutant.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Inyección de dependencias por constructor (Lombok)
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    public boolean analyzeDna(String[] dna) {
        // 1. Calcular Hash
        String dnaHash = calculateHash(dna);

        // 2. Verificar caché en BD (Evita re-procesamiento)
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);
        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }

        // 3. Ejecutar algoritmo si es nuevo
        boolean isMutant = mutantDetector.isMutant(dna);

        // 4. Guardar resultado
        DnaRecord record = DnaRecord.builder()
                .dnaHash(dnaHash)
                .isMutant(isMutant)
                .build();
        dnaRecordRepository.save(record);

        return isMutant;
    }

    private String calculateHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Unimos el array para hacer el hash de toda la secuencia
            String rawDna = String.join("", dna);
            byte[] encodedhash = digest.digest(rawDna.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculando hash SHA-256", e);
        }
    }
}