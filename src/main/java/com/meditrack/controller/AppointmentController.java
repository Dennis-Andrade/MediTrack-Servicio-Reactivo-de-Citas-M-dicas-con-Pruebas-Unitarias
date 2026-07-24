package com.meditrack.controller;

import com.meditrack.model.Appointment;
import com.meditrack.service.AppointmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public Flux<Appointment> getAppointments() {
        return appointmentService.getValidAppointments();
    }

    @GetMapping("/{id}")
    public Mono<Appointment> getAppointmentById(@PathVariable String id) {
        return appointmentService.findById(id);
    }
}
