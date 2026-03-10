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
import ro.unibuc.prodeng.model.ClientEntity;
import ro.unibuc.prodeng.request.CreateClientRequest;
import ro.unibuc.prodeng.service.AutoServiceCatalogService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private AutoServiceCatalogService catalogService;

    @PostMapping
    public ResponseEntity<ClientEntity> createClient(@Valid @RequestBody CreateClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createClient(request));
    }

    @GetMapping
    public ResponseEntity<List<ClientEntity>> getAllClients() {
        return ResponseEntity.ok(catalogService.getAllClients());
    }
}
