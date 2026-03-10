package ro.unibuc.prodeng.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ro.unibuc.prodeng.model.MechanicEntity;

@Repository
public interface MechanicRepository extends MongoRepository<MechanicEntity, String> {
}
