package github.com.berdnaski.sankhya_api.service;

import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import github.com.berdnaski.sankhya_api.domain.repositories.CustomerRepository;
import github.com.berdnaski.sankhya_api.rest.dto.CreateCustomerDTO;
import github.com.berdnaski.sankhya_api.rest.dto.ListCustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Customer> getCustomerById(String id) {
        return customerRepository.findById(UUID.fromString(id));
    }

    public Optional<ListCustomerDTO> findByPhone(String phone) {
        return customerRepository
                    .findByPhone(phone)
                .map(customer -> new ListCustomerDTO(customer.getName(), customer.getPhone()));
    }
}
