package github.com.berdnaski.sankhya_api.service;

import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import github.com.berdnaski.sankhya_api.domain.repositories.CustomerRepository;
import github.com.berdnaski.sankhya_api.rest.dto.CreateCustomerDTO;
import github.com.berdnaski.sankhya_api.rest.dto.ListCustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

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

    public Customer getCustomerById(String customerId) {
        return customerRepository.findById(UUID.fromString(customerId)).orElseThrow(() -> new RuntimeException("Customer id not found: " + customerId));
    }


    public Optional<ListCustomerDTO> findByPhone(String phone) {
        return customerRepository
                    .findByPhone(phone)
                .map(customer -> new ListCustomerDTO(customer.getName(), customer.getPhone()));
    }

    public List<ListCustomerDTO> listAll() {
        return customerRepository.findAll().stream()
                .map(customer -> new ListCustomerDTO(
                        customer.getName(),
                        customer.getPhone()
                )).collect(Collectors.toList());
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
