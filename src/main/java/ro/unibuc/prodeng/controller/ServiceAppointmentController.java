package ro.unibuc.prodeng.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ro.unibuc.prodeng.model.ServiceAppointmentEntity;
import ro.unibuc.prodeng.request.CreateServiceAppointmentRequest;
import ro.unibuc.prodeng.service.ServiceAppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class ServiceAppointmentController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @PostMapping
    public ResponseEntity<ServiceAppointmentEntity> createAppointment(
            @Valid @RequestBody CreateServiceAppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceAppointmentService.createAppointment(request));
    }

    @GetMapping("/by-mechanic/{mechanicId}")
    public ResponseEntity<List<ServiceAppointmentEntity>> getAppointmentsByMechanic(@PathVariable String mechanicId) {
        return ResponseEntity.ok(serviceAppointmentService.getAppointmentsByMechanic(mechanicId));
    }
}
