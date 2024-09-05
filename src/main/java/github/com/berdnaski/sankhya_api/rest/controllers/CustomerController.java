package github.com.berdnaski.sankhya_api.rest.controllers;

import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import github.com.berdnaski.sankhya_api.rest.dto.CreateCustomerDTO;
import github.com.berdnaski.sankhya_api.rest.dto.ListCustomerDTO;
import github.com.berdnaski.sankhya_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerDTO createCustomerDTO) {
        Customer createdCustomer = customerService.createCustomer(createCustomerDTO);
        return ResponseEntity.ok(createdCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<ListCustomerDTO> getCustomerByPhone(@PathVariable String phone) {
        var customer = customerService.findByPhone(phone);

        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ListCustomerDTO>> listAll() {
        List<ListCustomerDTO> customers = customerService.listAll();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody CreateCustomerDTO updateCustomerDTO) {
        customerService.updateCustomerById(id, updateCustomerDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }
}