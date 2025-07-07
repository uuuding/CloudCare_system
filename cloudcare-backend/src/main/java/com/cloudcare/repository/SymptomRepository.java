package com.cloudcare.repository;

import com.cloudcare.entity.Symptom;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SymptomRepository extends Neo4jRepository<Symptom, Long> {

    Symptom findByName(String name);

}