package ro.unibuc.prodeng.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.unibuc.prodeng.exception.EntityNotFoundException;
import ro.unibuc.prodeng.model.OrderStatus;
import ro.unibuc.prodeng.model.PartEntity;
import ro.unibuc.prodeng.model.RequiredPart;
import ro.unibuc.prodeng.model.ServiceOrderEntity;
import ro.unibuc.prodeng.repository.PartRepository;
import ro.unibuc.prodeng.repository.ServiceOrderRepository;
import ro.unibuc.prodeng.request.CreateServiceOrderRequest;
import ro.unibuc.prodeng.request.RequiredPartRequest;
import ro.unibuc.prodeng.response.RequiredPartResponse;
import ro.unibuc.prodeng.response.ServiceOrderResponse;

@Service
public class ServiceOrderService {

    private final AutoServiceCatalogService catalogService;
    private final ServiceOrderRepository serviceOrderRepository;
    private final PartRepository partRepository;

    @Autowired
    public ServiceOrderService(
            AutoServiceCatalogService catalogService,
            ServiceOrderRepository serviceOrderRepository,
            PartRepository partRepository) {
        this.catalogService = catalogService;
        this.serviceOrderRepository = serviceOrderRepository;
        this.partRepository = partRepository;
    }

    public ServiceOrderResponse createServiceOrder(CreateServiceOrderRequest request) {
        catalogService.ensureCarExists(request.carId());
        catalogService.ensureMechanicExists(request.mechanicId());

        List<PartEntity> partsToUpdate = new ArrayList<>();
        List<RequiredPart> requiredParts = new ArrayList<>();
        BigDecimal totalPartsCost = BigDecimal.ZERO;

        for (RequiredPartRequest requiredPartRequest : request.requiredParts()) {
            PartEntity part = catalogService.ensurePartExists(requiredPartRequest.partId());
            if (part.availableStock() < requiredPartRequest.quantity()) {
                throw new IllegalArgumentException("Not enough stock for part " + part.id()
                        + ". Required: " + requiredPartRequest.quantity()
                        + ", available: " + part.availableStock());
            }

            partsToUpdate.add(new PartEntity(
                    part.id(),
                    part.name(),
                    part.availableStock() - requiredPartRequest.quantity(),
                    part.unitPrice(),
                    part.supplierId()
            ));

            requiredParts.add(new RequiredPart(part.id(), requiredPartRequest.quantity(), part.unitPrice()));
            totalPartsCost = totalPartsCost.add(part.unitPrice().multiply(BigDecimal.valueOf(requiredPartRequest.quantity())));
        }

        partRepository.saveAll(partsToUpdate);

        BigDecimal totalCost = request.laborCost().add(totalPartsCost);
        ServiceOrderEntity saved = serviceOrderRepository.save(new ServiceOrderEntity(
                null,
                request.carId(),
                request.mechanicId(),
                request.serviceName(),
                request.description(),
                request.laborCost(),
                totalPartsCost,
                totalCost,
                requiredParts,
                request.scheduledAt(),
                null,
                OrderStatus.IN_PROGRESS
        ));

        return toResponse(saved);
    }

    public ServiceOrderResponse completeOrder(String orderId) {
        ServiceOrderEntity existing = getOrderEntity(orderId);
        if (existing.status() == OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("Order is already completed: " + orderId);
        }

        ServiceOrderEntity completed = new ServiceOrderEntity(
                existing.id(),
                existing.carId(),
                existing.mechanicId(),
                existing.serviceName(),
                existing.description(),
                existing.laborCost(),
                existing.partsCost(),
                existing.totalCost(),
                existing.requiredParts(),
                existing.scheduledAt(),
                LocalDateTime.now(),
                OrderStatus.COMPLETED
        );

        return toResponse(serviceOrderRepository.save(completed));
    }

    public ServiceOrderResponse getOrderById(String orderId) {
        return toResponse(getOrderEntity(orderId));
    }

    public List<ServiceOrderResponse> getOrdersByStatus(OrderStatus status) {
        return serviceOrderRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList();
    }

    private ServiceOrderEntity getOrderEntity(String orderId) {
        return serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("ServiceOrder " + orderId));
    }

    private ServiceOrderResponse toResponse(ServiceOrderEntity serviceOrderEntity) {
        List<RequiredPartResponse> requiredPartResponses = serviceOrderEntity.requiredParts().stream()
                .map(part -> new RequiredPartResponse(part.partId(), part.quantity(), part.unitPrice()))
                .toList();

        return new ServiceOrderResponse(
                serviceOrderEntity.id(),
                serviceOrderEntity.carId(),
                serviceOrderEntity.mechanicId(),
                serviceOrderEntity.serviceName(),
                serviceOrderEntity.description(),
                serviceOrderEntity.laborCost(),
                serviceOrderEntity.partsCost(),
                serviceOrderEntity.totalCost(),
                requiredPartResponses,
                serviceOrderEntity.scheduledAt(),
                serviceOrderEntity.completedAt(),
                serviceOrderEntity.status()
        );
    }
}
