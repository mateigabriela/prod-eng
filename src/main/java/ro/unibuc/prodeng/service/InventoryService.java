package ro.unibuc.prodeng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.unibuc.prodeng.model.PartEntity;
import ro.unibuc.prodeng.request.ReceiveDeliveryRequest;
import ro.unibuc.prodeng.repository.PartRepository;

@Service
public class InventoryService {

    @Autowired
    private AutoServiceCatalogService catalogService;

    @Autowired
    private PartRepository partRepository;

    public PartEntity receiveDelivery(ReceiveDeliveryRequest request) {
        PartEntity part = catalogService.ensurePartExists(request.partId());
        if (!part.supplierId().equals(request.supplierId())) {
            throw new IllegalArgumentException("Part " + request.partId()
                    + " does not belong to supplier " + request.supplierId());
        }

        PartEntity updatedPart = new PartEntity(
                part.id(),
                part.name(),
                part.availableStock() + request.quantity(),
                part.unitPrice(),
                part.supplierId()
        );

        return partRepository.save(updatedPart);
    }
}
