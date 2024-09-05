package github.com.berdnaski.sankhya_api.domain.repositories;

import github.com.berdnaski.sankhya_api.domain.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
}
