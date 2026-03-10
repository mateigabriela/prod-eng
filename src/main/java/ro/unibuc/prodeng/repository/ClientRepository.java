package ro.unibuc.prodeng.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ro.unibuc.prodeng.model.ClientEntity;

@Repository
public interface ClientRepository extends MongoRepository<ClientEntity, String> {
    Optional<ClientEntity> findByEmail(String email);
}
