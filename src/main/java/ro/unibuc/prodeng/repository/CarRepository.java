package ro.unibuc.prodeng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ro.unibuc.prodeng.model.CarEntity;

@Repository
public interface CarRepository extends MongoRepository<CarEntity, String> {
    List<CarEntity> findByClientId(String clientId);
    Optional<CarEntity> findByPlateNumber(String plateNumber);
}
