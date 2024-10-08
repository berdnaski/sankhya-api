package github.com.berdnaski.sankhya_api.rest.dto;

import java.time.LocalDateTime;

public record AppointmentDTO(
        String description,
        String appointmentDescription,
        LocalDateTime appointmentDate,
        String customerId
) {
    public AppointmentDTO withCustomerId(String newCustomerId) {
        return new AppointmentDTO(
                this.description,
                this.appointmentDescription,
                this.appointmentDate,
                newCustomerId
        );
    }
}
