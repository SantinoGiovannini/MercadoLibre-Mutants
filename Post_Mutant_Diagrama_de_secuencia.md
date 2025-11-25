sequenceDiagram
actor Client
participant Controller as MutantController
participant DTO as DnaRequest (DTO)
participant Service as MutantService
participant Algo as MutantDetector
participant Repo as DnaRecordRepository
participant DB as H2 Database

    Client->>Controller: POST /mutant {dna: [...]}
    Controller->>DTO: @Valid (Validar NxN, Caracteres)
    alt DNA Inválido
        DTO-->>Controller: Error Validación
        Controller-->>Client: 400 Bad Request
    else DNA Válido
        Controller->>Service: analyzeDna(dna)
        Service->>Service: calculateHash(dna) (SHA-256)
        
        Service->>Repo: findByDnaHash(hash)
        Repo->>DB: SELECT * FROM dna_records WHERE hash = ?
        DB-->>Repo: Resultado (Optional)
        Repo-->>Service: DnaRecord (si existe)

        alt Ya existe en BD (Caché Hit)
            Service-->>Controller: isMutant (recuperado)
        else No existe (Nuevo análisis)
            Service->>Algo: isMutant(dna)
            Algo-->>Service: true/false
            Service->>Repo: save(new DnaRecord)
            Repo->>DB: INSERT INTO dna_records ...
            Service-->>Controller: resultado calculado
        end

        alt es Mutante
            Controller-->>Client: 200 OK
        else es Humano
            Controller-->>Client: 403 Forbidden
        end
    end