package ro.unibuc.prodeng.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "service_appointments")
public record ServiceAppointmentEntity(
        @Id String id,
        String carId,
        String mechanicId,
        LocalDateTime scheduledAt,
        List<AppointmentReason> reasons,
        int estimatedDurationMinutes,
        LocalDateTime estimatedEndAt,
        LocalDateTime createdAt
) {
}
