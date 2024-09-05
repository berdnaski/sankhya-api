package github.com.berdnaski.sankhya_api.domain.repositories;

import github.com.berdnaski.sankhya_api.domain.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByCustomerId(UUID customerId);

    boolean existsByCustomerId(UUID customerId);
}
