package github.com.berdnaski.sankhya_api.service;

import github.com.berdnaski.sankhya_api.domain.entities.Appointment;
import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import github.com.berdnaski.sankhya_api.domain.repositories.AppointmentRepository;
import github.com.berdnaski.sankhya_api.domain.repositories.CustomerRepository;
import github.com.berdnaski.sankhya_api.rest.dto.AppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;

    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        Customer customer = customerRepository.findById(UUID.fromString(appointmentDTO.customerId()))
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + appointmentDTO.customerId()));

        Appointment appointment = new Appointment(
                UUID.randomUUID(),
                appointmentDTO.description(),
                appointmentDTO.appointmentDate(),
                customer
        );
        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointmentById(String appointmentId) {
        return appointmentRepository.findById(UUID.fromString(appointmentId))
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
}
