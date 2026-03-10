package ro.unibuc.prodeng.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "suppliers")
public record SupplierEntity(
        @Id String id,
        String name,
        String address,
        String phone
) {
}
