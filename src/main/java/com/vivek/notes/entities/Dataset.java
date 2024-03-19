package com.vivek.notes.entities;

import com.vivek.notes.enums.DatasetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer datasetId;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @Enumerated(EnumType.STRING)
    private DatasetType type;

    private String filePath; // Path to the stored dataset file
}

