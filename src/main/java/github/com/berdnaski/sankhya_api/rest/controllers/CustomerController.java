package github.com.berdnaski.sankhya_api.rest.controllers;

import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import github.com.berdnaski.sankhya_api.rest.dto.CreateCustomerDTO;
import github.com.berdnaski.sankhya_api.rest.dto.ListCustomerDTO;
import github.com.berdnaski.sankhya_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        var customer = customerService.getCustomerById(id);

        if(customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<ListCustomerDTO> getCustomerByPhone(@PathVariable String phone) {
        var customer = customerService.findByPhone(phone);

        if(customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
