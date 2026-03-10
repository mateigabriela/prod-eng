package ro.unibuc.prodeng.response;

import java.math.BigDecimal;

public record RequiredPartResponse(
        String partId,
        int quantity,
        BigDecimal unitPrice
) {
}
