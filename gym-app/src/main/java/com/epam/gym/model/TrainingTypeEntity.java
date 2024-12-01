package com.epam.gym.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "training_type")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_type_seq")
    @SequenceGenerator(name = "training_type_seq", sequenceName = "training_type_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "training_type_name", nullable = false)
    private String trainingTypeName;
}
