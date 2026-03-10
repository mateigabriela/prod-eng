package ro.unibuc.prodeng.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
public record ClientEntity(
        @Id String id,
        String firstName,
        String lastName,
        String phone,
        String email,
        String address
) {
}
