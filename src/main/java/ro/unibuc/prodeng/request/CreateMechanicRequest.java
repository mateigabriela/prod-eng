package ro.unibuc.prodeng.request;

import jakarta.validation.constraints.NotBlank;

public record CreateMechanicRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phone
) {
}
