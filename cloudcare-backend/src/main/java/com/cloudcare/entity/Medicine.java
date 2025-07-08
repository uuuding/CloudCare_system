package com.cloudcare.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
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

}