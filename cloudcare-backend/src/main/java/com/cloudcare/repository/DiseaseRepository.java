package com.cloudcare.repository;

import com.cloudcare.entity.Disease;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiseaseRepository extends Neo4jRepository<Disease, Long> {

    Disease findByName(String name);

    @Query("MATCH (d:Disease)-[:有症状]->(s:Symptom) WHERE s.name = $symptomName RETURN d")
    List<Disease> findBySymptomName(@Param("symptomName") String symptomName);
}