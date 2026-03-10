package ro.unibuc.prodeng.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCarRequest(
        @NotBlank String brand,
        @NotBlank String model,
        @Min(1950) @Max(2100) int fabricationYear,
        @NotBlank String plateNumber,
        @NotNull String clientId
) {
}
