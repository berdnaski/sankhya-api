package github.com.berdnaski.sankhya_api.rest.controllers;

import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import github.com.berdnaski.sankhya_api.domain.repositories.CustomerRepository;
import github.com.berdnaski.sankhya_api.rest.dto.CreateCustomerDTO;
import github.com.berdnaski.sankhya_api.rest.dto.LoginRequestDTO;
import github.com.berdnaski.sankhya_api.rest.dto.ResponseDTO;
import github.com.berdnaski.sankhya_api.security.config.TokenService;
import github.com.berdnaski.sankhya_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CustomerService customerService;
    private final TokenService tokenService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateCustomerDTO body) {
        Optional<Customer> customer = this.customerRepository.findByPhone(body.phone());

        if(customer.isEmpty()) {
            Customer newCustomer = new Customer();
            newCustomer.setPassword(passwordEncoder.encode(body.password()));
            newCustomer.setPhone(body.phone());
            newCustomer.setName(body.name());
            this.customerRepository.save(newCustomer);

            String token = this.tokenService.generateToken(newCustomer);
            return ResponseEntity.ok(new ResponseDTO(newCustomer.getName(), newCustomer.getPhone(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        Customer customer = this.customerRepository.findByPhone(body.phone()).orElseThrow(() -> new RuntimeException("Customer not found"));
        if(passwordEncoder.matches(body.password(), customer.getPassword())) {
            String token = this.tokenService.generateToken(customer);
            return ResponseEntity.ok(new ResponseDTO(customer.getName(), customer.getPhone(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
