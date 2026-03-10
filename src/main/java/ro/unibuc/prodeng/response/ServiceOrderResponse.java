package ro.unibuc.prodeng.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import ro.unibuc.prodeng.model.OrderStatus;

public record ServiceOrderResponse(
        String id,
        String carId,
        String mechanicId,
        String serviceName,
        String description,
        BigDecimal laborCost,
        BigDecimal partsCost,
        BigDecimal totalCost,
        List<RequiredPartResponse> requiredParts,
        LocalDateTime scheduledAt,
        LocalDateTime completedAt,
        OrderStatus status
) {
}
