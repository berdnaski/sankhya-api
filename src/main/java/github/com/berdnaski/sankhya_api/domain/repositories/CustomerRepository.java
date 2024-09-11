package github.com.berdnaski.sankhya_api.domain.repositories;

import github.com.berdnaski.sankhya_api.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByPhone(String phone);
    Optional<Customer> findByPhone(String phone);
}
