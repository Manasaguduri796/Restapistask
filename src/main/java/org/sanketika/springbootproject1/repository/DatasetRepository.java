package org.sanketika.springbootproject1.repository;


import org.sanketika.springbootproject1.entity.Dataset;
import org.sanketika.springbootproject1.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset,String> {
  List<Dataset> findByStatus(Status status);
}