package ro.unibuc.prodeng.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ro.unibuc.prodeng.model.AppointmentReason;

public record CreateServiceAppointmentRequest(
        @NotBlank String carId,
        @NotBlank String mechanicId,
        @NotNull LocalDateTime scheduledAt,
        @NotEmpty List<@NotNull AppointmentReason> reasons
) {
}
