package ro.unibuc.prodeng.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RequiredPartRequest(
        @NotBlank String partId,
        @Min(1) int quantity
) {
}
