package ro.unibuc.prodeng.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cars")
public record CarEntity(
        @Id String id,
        String brand,
        String model,
        int fabricationYear,
        String plateNumber,
        String clientId
) {
}
