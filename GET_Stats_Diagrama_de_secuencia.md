sequenceDiagram
actor Client
participant Controller as MutantController
participant Service as StatsService
participant Repo as DnaRecordRepository
participant DB as H2 Database

    Client->>Controller: GET /stats
    Controller->>Service: getStats()
    
    par Consultas Paralelas (o secuenciales optimizadas)
        Service->>Repo: countByIsMutant(true)
        Repo->>DB: SELECT COUNT(*) ... WHERE is_mutant = true
        DB-->>Repo: countMutant (long)
        
        Service->>Repo: countByIsMutant(false)
        Repo->>DB: SELECT COUNT(*) ... WHERE is_mutant = false
        DB-->>Repo: countHuman (long)
    end

    Service->>Service: calcular Ratio (mutant / human)
    Service-->>Controller: StatsResponse {count_mutant, count_human, ratio}
    Controller-->>Client: 200 OK (JSON)