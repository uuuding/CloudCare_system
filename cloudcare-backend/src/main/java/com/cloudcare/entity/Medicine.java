package com.cloudcare.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("Medicine")
public class Medicine {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private String dosage; // 剂量

    private String frequency; // 用药频率

    private String sideEffects; // 副作用

    @Relationship(type = "治疗", direction = Relationship.Direction.OUTGOING)
    private Set<Disease> treatedDiseases = new HashSet<>();

    public Medicine() {}

    public Medicine(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Medicine(String name, String description, String dosage, String frequency) {
        this.name = name;
        this.description = description;
        this.dosage = dosage;
        this.frequency = frequency;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public Set<Disease> getTreatedDiseases() {
        return treatedDiseases;
    }

    public void setTreatedDiseases(Set<Disease> treatedDiseases) {
        this.treatedDiseases = treatedDiseases;
    }
}