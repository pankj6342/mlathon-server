package com.vivek.notes.repositories;

import com.vivek.notes.entities.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatasetRepository extends JpaRepository<Dataset, Integer> {
}
