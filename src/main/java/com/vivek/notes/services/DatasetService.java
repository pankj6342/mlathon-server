package com.vivek.notes.services;

import com.vivek.notes.entities.Dataset;
import com.vivek.notes.repositories.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class DatasetService {
    @Autowired
    private DatasetRepository datasetRepository;

    // Assuming file storage is handled elsewhere
    public Dataset saveDataset(Dataset dataset) {
        return datasetRepository.save(dataset);
    }

    public Optional<Dataset> getDatasetById(int datasetId) {
        return datasetRepository.findById(datasetId);
    }

    // Implement functionalities for uploading/downloading datasets based on your storage solution
}
