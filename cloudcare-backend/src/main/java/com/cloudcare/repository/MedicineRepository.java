package com.cloudcare.repository;

import com.cloudcare.entity.Medicine;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicineRepository extends Neo4jRepository<Medicine, Long> {

    Medicine findByName(String name);

    @Query("MATCH (m:Medicine)-[:治疗]->(d:Disease) WHERE d.name = $diseaseName RETURN m")
    List<Medicine> findByDiseaseName(@Param("diseaseName") String diseaseName);

    @Query("MATCH (m:Medicine)-[:治疗]->(d:Disease) WHERE d.id = $diseaseId RETURN m")
    List<Medicine> findByDiseaseId(@Param("diseaseId") Long diseaseId);
}