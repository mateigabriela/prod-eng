package ro.unibuc.prodeng.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parts")
public record PartEntity(
        @Id String id,
        String name,
        int availableStock,
        BigDecimal unitPrice,
        String supplierId
) {
}
