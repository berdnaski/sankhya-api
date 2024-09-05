package github.com.berdnaski.sankhya_api.service;

import github.com.berdnaski.sankhya_api.domain.entities.Appointment;
import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import github.com.berdnaski.sankhya_api.domain.repositories.AppointmentRepository;
import github.com.berdnaski.sankhya_api.domain.repositories.CustomerRepository;
import github.com.berdnaski.sankhya_api.rest.dto.AppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;

    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        Customer customer = customerRepository.findById(UUID.fromString(appointmentDTO.customerId()))
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + appointmentDTO.customerId()));

        boolean customerHasAppointment = appointmentRepository.existsByCustomer(customer);

        if(customerHasAppointment) {
            throw new RuntimeException("Customer already has an appointment");
        }

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

    public List<Appointment> listAll() {
        return appointmentRepository.findAll();
    }

    public void updateAppointmentsById(UUID appointmentId, AppointmentDTO updateAppointmentDTO) {
        var appointmentEntity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));

        if (updateAppointmentDTO.description() != null && !updateAppointmentDTO.description().equals(appointmentEntity.getDescription())) {
            appointmentEntity.setDescription(updateAppointmentDTO.description());
        }

        if (updateAppointmentDTO.appointmentDescription() != null && !updateAppointmentDTO.appointmentDescription().equals(appointmentEntity.getDescription())) {
            appointmentEntity.setDescription(updateAppointmentDTO.appointmentDescription()); // Atualiza a descrição, se for esse o caso
        }

        if (updateAppointmentDTO.appointmentDate() != null && !updateAppointmentDTO.appointmentDate().equals(appointmentEntity.getAppointmentDate())) {
            appointmentEntity.setAppointmentDate(updateAppointmentDTO.appointmentDate());
        }

        if (updateAppointmentDTO.customerId() != null) {
            Customer customer = customerRepository.findById(UUID.fromString(updateAppointmentDTO.customerId()))
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + updateAppointmentDTO.customerId()));

            boolean hasExistingAppointment = appointmentRepository.existsByCustomer(customer);
            if (hasExistingAppointment && !customer.equals(appointmentEntity.getCustomer())) {
                throw new IllegalArgumentException("Customer already has an existing appointment");
            }

            appointmentEntity.setCustomer(customer);
        }

        appointmentRepository.save(appointmentEntity);
    }

    public void deleteAppointmentById(String appointmentId) {
        UUID id = UUID.fromString(appointmentId);
        Optional<Appointment> appointmentExists = appointmentRepository.findById(id);

        if(appointmentExists.isPresent()) {
            appointmentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Appointment not found with ID: " + appointmentId);
        }
    }
}
