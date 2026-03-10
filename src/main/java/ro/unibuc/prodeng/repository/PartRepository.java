package ro.unibuc.prodeng.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ro.unibuc.prodeng.model.PartEntity;

@Repository
public interface PartRepository extends MongoRepository<PartEntity, String> {
}
