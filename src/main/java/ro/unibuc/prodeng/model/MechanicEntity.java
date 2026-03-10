package ro.unibuc.prodeng.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mechanics")
public record MechanicEntity(
        @Id String id,
        String firstName,
        String lastName,
        String phone
) {
}
