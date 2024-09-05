package github.com.berdnaski.sankhya_api.rest.controllers;

import github.com.berdnaski.sankhya_api.domain.entities.Appointment;
import github.com.berdnaski.sankhya_api.rest.dto.AppointmentDTO;
import github.com.berdnaski.sankhya_api.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDTO createAppointmentDTO) {
        Appointment createdAppointment = appointmentService.createAppointment(createAppointmentDTO);
        return ResponseEntity.ok(createdAppointment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable String id) {
        try {
            Appointment appointmentDTO = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(appointmentDTO);
        } catch (RuntimeException e ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
