package com.meditrack.service;

import com.meditrack.model.Appointment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

@Service
public class AppointmentService {

    public Flux<Appointment> getValidAppointments() {
        return getAppointments()
                // filter deja pasar solo las citas que cumplen la regla de negocio.
                .filter(this::isValidAppointment)
                // map transforma cada cita sin bloquear; aqui normaliza la especialidad.
                .map(this::normalizeSpecialty)
                // defaultIfEmpty entrega una cita generica si todas fueron descartadas.
                .defaultIfEmpty(defaultAppointment());
    }

    public Mono<Appointment> findById(String id) {
        return getValidAppointments()
                // filter busca el id dentro del flujo reactivo, sin convertirlo a List.
                .filter(appointment -> appointment.getId().equals(id))
                .next()
                // switchIfEmpty representa el caso no encontrado como error reactivo.
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No existe una cita con id: " + id)));
    }

    protected Flux<Appointment> getAppointments() {
        return Flux.just(
                new Appointment("A-001", "Ana Torres", "Cardiologia", 45.0, Arrays.asList("ana@mail.com")),
                new Appointment("A-002", "Luis Vera", "Pediatria", 35.0, Arrays.asList("luis@mail.com")),
                new Appointment("A-003", "Marta Diaz", "Dermatologia", 40.0, Arrays.asList("marta@mail.com")),
                new Appointment("A-004", "Carlos Ruiz", "Traumatologia", 0.0, Arrays.asList("carlos@mail.com")),
                new Appointment("A-005", "Sofia Mora", "Neurologia", 60.0, Collections.emptyList())
        );
    }

    private boolean isValidAppointment(Appointment appointment) {
        return appointment.getCostUsd() > 0 && !appointment.getNotifyEmails().isEmpty();
    }

    private Appointment normalizeSpecialty(Appointment appointment) {
        return new Appointment(
                appointment.getId(),
                appointment.getPatientName(),
                appointment.getSpecialty().toUpperCase(Locale.ROOT),
                appointment.getCostUsd(),
                appointment.getNotifyEmails()
        );
    }

    private Appointment defaultAppointment() {
        return new Appointment(
                "DEFAULT",
                "Paciente pendiente",
                "GENERAL",
                1.0,
                Arrays.asList("recepcion@meditrack.local")
        );
    }
}
