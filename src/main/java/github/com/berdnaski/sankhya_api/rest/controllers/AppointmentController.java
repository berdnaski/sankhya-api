package github.com.berdnaski.sankhya_api.rest.controllers;

import github.com.berdnaski.sankhya_api.domain.entities.Appointment;
import github.com.berdnaski.sankhya_api.rest.dto.AppointmentDTO;
import github.com.berdnaski.sankhya_api.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers/{customerId}/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@PathVariable UUID customerId, @RequestBody AppointmentDTO createAppointmentDTO) {
        createAppointmentDTO = createAppointmentDTO.withCustomerId(customerId.toString());
        Appointment createdAppointment = appointmentService.createAppointment(createAppointmentDTO);

        return ResponseEntity.ok(createdAppointment);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> listAppointmentsByCustomer(@PathVariable UUID customerId) {
        List<Appointment> appointments = appointmentService.listAppointmentsByCustomer(customerId);

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID customerId, @PathVariable UUID id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            if (!appointment.getCustomer().getId().equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(appointment);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID customerId, @PathVariable UUID id, @RequestBody AppointmentDTO updateAppointmentDTO) {
        updateAppointmentDTO = updateAppointmentDTO.withCustomerId(customerId.toString());
        appointmentService.updateAppointmentsById(id, updateAppointmentDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID customerId, @PathVariable UUID id) {
        appointmentService.deleteAppointmentById(id);

        return ResponseEntity.noContent().build();
    }
}
