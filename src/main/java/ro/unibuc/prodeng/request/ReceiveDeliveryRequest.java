package ro.unibuc.prodeng.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceiveDeliveryRequest(
        @NotBlank String partId,
        @NotBlank String supplierId,
        @Min(1) int quantity,
        @NotNull LocalDateTime deliveredAt
) {
}
