package github.com.berdnaski.sankhya_api.service;

import github.com.berdnaski.sankhya_api.domain.entities.Appointment;
import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import github.com.berdnaski.sankhya_api.domain.repositories.AppointmentRepository;
import github.com.berdnaski.sankhya_api.domain.repositories.CustomerRepository;
import github.com.berdnaski.sankhya_api.rest.dto.AppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;

    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        Customer customer = customerRepository.findById(UUID.fromString(appointmentDTO.customerId()))
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + appointmentDTO.customerId()));

        if (appointmentRepository.existsByCustomerId(customer.getId())) {
            throw new IllegalArgumentException("Customer already has an appointment");
        }

        Appointment appointment = new Appointment(
                UUID.randomUUID(),
                appointmentDTO.description(),
                appointmentDTO.appointmentDate(),
                customer
        );
        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointmentById(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public List<Appointment> listAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> listAppointmentsByCustomer(UUID customerId) {
        return appointmentRepository.findByCustomerId(customerId);
    }

    public void updateAppointmentsById(UUID id, AppointmentDTO updateAppointmentDTO) {
        var appointmentEntity = appointmentRepository.findById(id);

        if (appointmentEntity.isPresent()) {
            var appointment = appointmentEntity.get();

            if (updateAppointmentDTO.description() != null) {
                appointment.setDescription(updateAppointmentDTO.description());
            }
            if (updateAppointmentDTO.appointmentDate() != null) {
                appointment.setAppointmentDate(updateAppointmentDTO.appointmentDate());
            }

            appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Appointment not found");
        }
    }

    public void deleteAppointmentById(UUID id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Appointment not found");
        }
    }
}
