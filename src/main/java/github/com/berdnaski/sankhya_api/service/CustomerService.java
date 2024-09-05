package github.com.berdnaski.sankhya_api.service;

import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import github.com.berdnaski.sankhya_api.domain.repositories.CustomerRepository;
import github.com.berdnaski.sankhya_api.domain.repositories.AppointmentRepository;
import github.com.berdnaski.sankhya_api.rest.dto.AppointmentDTO;
import github.com.berdnaski.sankhya_api.rest.dto.CreateCustomerDTO;
import github.com.berdnaski.sankhya_api.rest.dto.ListCustomerDTO;
import github.com.berdnaski.sankhya_api.rest.dto.PhoneInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;

    public Customer createCustomer(CreateCustomerDTO createCustomerDTO) {
        if(customerRepository.existsByPhone(createCustomerDTO.phone())) {
            throw new IllegalArgumentException("Customer with given phone already exists");
        }

        Customer customer = new Customer(
                UUID.randomUUID(),
                createCustomerDTO.name(),
                createCustomerDTO.phone()
        );
        return customerRepository.save(customer);
    }

    public ListCustomerDTO getCustomerById(String customerId) {
        Customer customer = customerRepository.findById(UUID.fromString(customerId))
                .orElseThrow(() -> new RuntimeException("Customer id not found: " + customerId));

        List<AppointmentDTO> appointments = customer.getAppointments().stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getId().toString(),
                        appointment.getDescription(),
                        appointment.getAppointmentDate(),
                        customer.getId().toString()
                ))
                .collect(Collectors.toList());

        return new ListCustomerDTO(
                customer.getId().toString(),
                customer.getName(),
                customer.getPhone(),
                appointments
        );
    }

    public Optional<PhoneInfoDTO> findByPhone(String phone) {
        return customerRepository.findByPhone(phone)
                .map(customer -> new PhoneInfoDTO(
                        customer.getName(),
                        customer.getPhone()
                ));
    }

    public List<ListCustomerDTO> listAll() {
        return customerRepository.findAll().stream()
                .map(customer -> {
                    List<AppointmentDTO> appointments = customer.getAppointments().stream()
                            .map(appointment -> new AppointmentDTO(
                                    appointment.getId().toString(),
                                    appointment.getDescription(),
                                    appointment.getAppointmentDate(),
                                    customer.getId().toString()
                            ))
                            .collect(Collectors.toList());

                    return new ListCustomerDTO(
                            customer.getId().toString(),
                            customer.getName(),
                            customer.getPhone(),
                            appointments
                    );
                })
                .collect(Collectors.toList());
    }

    public void updateCustomerById(UUID customerId, CreateCustomerDTO updateCustomerDTO) {
        var customerEntity = customerRepository.findById(customerId);

        if (customerEntity.isPresent()) {
            var customer = customerEntity.get();

            if (updateCustomerDTO.phone() != null && !customer.getPhone().equals(updateCustomerDTO.phone())) {
                boolean phoneExists = customerRepository.existsByPhone(updateCustomerDTO.phone());
                if (phoneExists) {
                    throw new IllegalArgumentException("Customer with given phone already exists");
                }
            }

            if (updateCustomerDTO.name() != null) {
                customer.setName(updateCustomerDTO.name());
            }

            if (updateCustomerDTO.phone() != null) {
                customer.setPhone(updateCustomerDTO.phone());
            }

            customerRepository.save(customer);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    public void deleteCustomerById(String customerId) {
        UUID id = UUID.fromString(customerId);
        Optional<Customer> customerExists = customerRepository.findById(id);

        if (customerExists.isPresent()) {
            customerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }
}
