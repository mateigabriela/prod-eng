package ro.unibuc.prodeng.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.unibuc.prodeng.model.AppointmentReason;
import ro.unibuc.prodeng.model.ServiceAppointmentEntity;
import ro.unibuc.prodeng.repository.ServiceAppointmentRepository;
import ro.unibuc.prodeng.request.CreateServiceAppointmentRequest;

@Service
public class ServiceAppointmentService {

    private final AutoServiceCatalogService catalogService;
    private final ServiceAppointmentRepository serviceAppointmentRepository;

    @Autowired
    public ServiceAppointmentService(
            AutoServiceCatalogService catalogService,
            ServiceAppointmentRepository serviceAppointmentRepository) {
        this.catalogService = catalogService;
        this.serviceAppointmentRepository = serviceAppointmentRepository;
    }

    public ServiceAppointmentEntity createAppointment(CreateServiceAppointmentRequest request) {
        catalogService.ensureCarExists(request.carId());
        catalogService.ensureMechanicExists(request.mechanicId());

        if (request.scheduledAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date must be in the future");
        }

        List<AppointmentReason> reasons = request.reasons().stream().distinct().collect(Collectors.toList());
        int estimatedDurationMinutes = reasons.stream()
            .mapToInt(AppointmentReason::estimatedMinutes)
            .sum();
        LocalDateTime estimatedEndAt = request.scheduledAt().plusMinutes(estimatedDurationMinutes);

        return serviceAppointmentRepository.save(new ServiceAppointmentEntity(
                null,
                request.carId(),
                request.mechanicId(),
                request.scheduledAt(),
            reasons,
            estimatedDurationMinutes,
            estimatedEndAt,
                LocalDateTime.now()
        ));
    }

    public List<ServiceAppointmentEntity> getAppointmentsByMechanic(String mechanicId) {
        catalogService.ensureMechanicExists(mechanicId);
        return serviceAppointmentRepository.findByMechanicIdOrderByScheduledAtAsc(mechanicId);
    }
}
