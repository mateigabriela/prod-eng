package ro.unibuc.prodeng.model;

import java.math.BigDecimal;

public record RequiredPart(
        String partId,
        int quantity,
        BigDecimal unitPrice
) {
}
