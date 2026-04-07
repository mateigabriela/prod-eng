package ro.unibuc.prodeng.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ro.unibuc.prodeng.model.ServiceAppointmentEntity;

@Repository
public interface ServiceAppointmentRepository extends MongoRepository<ServiceAppointmentEntity, String> {
    List<ServiceAppointmentEntity> findByMechanicIdOrderByScheduledAtAsc(String mechanicId);
}
