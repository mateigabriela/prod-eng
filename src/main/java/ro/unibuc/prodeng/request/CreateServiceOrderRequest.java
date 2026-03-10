package ro.unibuc.prodeng.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateServiceOrderRequest(
        @NotBlank String carId,
        @NotBlank String mechanicId,
        @NotBlank String serviceName,
        @NotBlank String description,
        @DecimalMin("0.0") @NotNull BigDecimal laborCost,
        @NotNull LocalDateTime scheduledAt,
        @NotEmpty List<@Valid RequiredPartRequest> requiredParts
) {
}
