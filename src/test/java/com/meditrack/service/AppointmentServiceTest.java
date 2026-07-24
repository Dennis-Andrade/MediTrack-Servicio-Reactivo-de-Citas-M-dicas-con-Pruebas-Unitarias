package com.meditrack.service;

import com.meditrack.model.Appointment;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

public class AppointmentServiceTest {

    @Test
    public void getValidAppointments_debeEmitirSoloLasTresValidas() {
        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        Flux<Appointment> flujo = service.getValidAppointments();

        // Assert
        StepVerifier.create(flujo)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getValidAppointments_todasInvalidas_emiteCitaPorDefecto() {
        // Arrange
        AppointmentService service = new AppointmentService() {
            @Override
            protected Flux<Appointment> getAppointments() {
                return Flux.just(
                        new Appointment("A-010", "Paciente Uno", "General", 0.0, Arrays.asList("uno@mail.com")),
                        new Appointment("A-011", "Paciente Dos", "General", 20.0, Collections.emptyList())
                );
            }
        };

        // Act
        Flux<Appointment> flujo = service.getValidAppointments();

        // Assert
        StepVerifier.create(flujo)
                .expectNextMatches(appointment -> "DEFAULT".equals(appointment.getId()))
                .verifyComplete();
    }

    @Test
    public void findById_idInexistente_terminaEnError() {
        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        Mono<Appointment> resultado = service.findById("NO-EXISTE");

        // Assert
        StepVerifier.create(resultado)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
