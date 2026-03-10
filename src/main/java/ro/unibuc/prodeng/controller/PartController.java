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
import ro.unibuc.prodeng.model.PartEntity;
import ro.unibuc.prodeng.request.CreatePartRequest;
import ro.unibuc.prodeng.service.AutoServiceCatalogService;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    @Autowired
    private AutoServiceCatalogService catalogService;

    @PostMapping
    public ResponseEntity<PartEntity> createPart(@Valid @RequestBody CreatePartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createPart(request));
    }

    @GetMapping
    public ResponseEntity<List<PartEntity>> getAllParts() {
        return ResponseEntity.ok(catalogService.getAllParts());
    }
}
