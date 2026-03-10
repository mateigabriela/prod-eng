package ro.unibuc.prodeng.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePartRequest(
        @NotBlank String name,
        @Min(0) int availableStock,
        @DecimalMin("0.0") @NotNull BigDecimal unitPrice,
        @NotBlank String supplierId
) {
}
