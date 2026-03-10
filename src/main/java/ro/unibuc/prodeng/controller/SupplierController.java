package ro.unibuc.prodeng.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ro.unibuc.prodeng.model.SupplierEntity;
import ro.unibuc.prodeng.request.CreateSupplierRequest;
import ro.unibuc.prodeng.service.AutoServiceCatalogService;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private AutoServiceCatalogService catalogService;

    @PostMapping
    public ResponseEntity<SupplierEntity> createSupplier(@Valid @RequestBody CreateSupplierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createSupplier(request));
    }

    @GetMapping
    public ResponseEntity<List<SupplierEntity>> getAllSuppliers() {
        return ResponseEntity.ok(catalogService.getAllSuppliers());
    }
}
