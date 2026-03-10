package ro.unibuc.prodeng.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "service_orders")
public record ServiceOrderEntity(
        @Id String id,
        String carId,
        String mechanicId,
        String serviceName,
        String description,
        BigDecimal laborCost,
        BigDecimal partsCost,
        BigDecimal totalCost,
        List<RequiredPart> requiredParts,
        LocalDateTime scheduledAt,
        LocalDateTime completedAt,
        OrderStatus status
) {
}
