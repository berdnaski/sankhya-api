package github.com.berdnaski.sankhya_api.domain.repositories;

import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByPhone(String phone);
    Optional<Customer> findByPhone(String phone);
}
