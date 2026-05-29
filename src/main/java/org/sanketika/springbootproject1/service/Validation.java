package org.sanketika.springbootproject1.service;
import org.sanketika.springbootproject1.entity.Dataset;
import org.sanketika.springbootproject1.repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Validation {

    @Autowired
    private DatasetRepository datasetRepository;

    public static Optional<String> validate(Dataset dataset) {
        if (dataset.getId() == null || dataset.getId().isEmpty()) {
            return Optional.of("primary key id is required");
        }
        if (dataset.getDataSchema() == null || dataset.getDataSchema().isEmpty()) {
            return Optional.of("Dataschema is required");
        }
        if (dataset.getRouterConfig() == null || dataset.getRouterConfig().isEmpty()) {
            return Optional.of("Router config is required");
        }
        return Optional.empty();
    }

   public static Optional<String> validateForUpdate(Dataset updateDataset){
        if (updateDataset.getDataSchema() == null || updateDataset.getDataSchema().values().isEmpty()) {
           return Optional.of("Dataschema is required");
       }
       if (updateDataset.getRouterConfig() == null || updateDataset.getRouterConfig().values().isEmpty()) {
           return Optional.of("Router config is required");
       }
       return Optional.empty();
   }

}


