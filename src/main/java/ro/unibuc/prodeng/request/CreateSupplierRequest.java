package ro.unibuc.prodeng.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSupplierRequest(
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String phone
) {
}
