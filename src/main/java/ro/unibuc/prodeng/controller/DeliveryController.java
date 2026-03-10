package ro.unibuc.prodeng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ro.unibuc.prodeng.model.PartEntity;
import ro.unibuc.prodeng.request.ReceiveDeliveryRequest;
import ro.unibuc.prodeng.service.InventoryService;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @Autowired
    private InventoryService inventoryService;

    @PatchMapping("/receive")
    public ResponseEntity<PartEntity> receiveDelivery(@Valid @RequestBody ReceiveDeliveryRequest request) {
        return ResponseEntity.ok(inventoryService.receiveDelivery(request));
    }
}
