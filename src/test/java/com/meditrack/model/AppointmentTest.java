package com.meditrack.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class AppointmentTest {

    @Test
    public void getters_datosDelConstructor_devuelvenLosMismosValores() {
        // Arrange
        List<String> correos = Arrays.asList("ana@mail.com", "recepcion@mail.com");
        Appointment appointment = new Appointment("A-001", "Ana Torres", "Cardiologia", 45.0, correos);

        // Act
        String id = appointment.getId();
        String patientName = appointment.getPatientName();
        String specialty = appointment.getSpecialty();
        Double costUsd = appointment.getCostUsd();
        List<String> notifyEmails = appointment.getNotifyEmails();

        // Assert
        assertEquals("A-001", id);
        assertEquals("Ana Torres", patientName);
        assertEquals("Cardiologia", specialty);
        assertEquals(Double.valueOf(45.0), costUsd);
        assertEquals(correos, notifyEmails);
    }

    @Test
    public void getNotifyEmails_listaOriginalModificada_mantieneCopiaInterna() {
        // Arrange
        List<String> correos = new ArrayList<>(Arrays.asList("ana@mail.com"));
        Appointment appointment = new Appointment("A-001", "Ana Torres", "Cardiologia", 45.0, correos);

        // Act
        correos.add("externo@mail.com");
        List<String> notifyEmails = appointment.getNotifyEmails();

        // Assert
        assertEquals(1, notifyEmails.size());
        assertNotSame(correos, notifyEmails);
    }
}
