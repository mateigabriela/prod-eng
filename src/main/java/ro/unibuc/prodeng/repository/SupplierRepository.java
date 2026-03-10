package ro.unibuc.prodeng.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ro.unibuc.prodeng.model.SupplierEntity;

@Repository
public interface SupplierRepository extends MongoRepository<SupplierEntity, String> {
}
