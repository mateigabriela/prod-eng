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
import ro.unibuc.prodeng.model.MechanicEntity;
import ro.unibuc.prodeng.request.CreateMechanicRequest;
import ro.unibuc.prodeng.service.AutoServiceCatalogService;

@RestController
@RequestMapping("/api/mechanics")
public class MechanicController {

    @Autowired
    private AutoServiceCatalogService catalogService;

    @PostMapping
    public ResponseEntity<MechanicEntity> createMechanic(@Valid @RequestBody CreateMechanicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createMechanic(request));
    }

    @GetMapping
    public ResponseEntity<List<MechanicEntity>> getAllMechanics() {
        return ResponseEntity.ok(catalogService.getAllMechanics());
    }
}
