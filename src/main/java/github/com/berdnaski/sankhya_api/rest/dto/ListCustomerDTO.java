package github.com.berdnaski.sankhya_api.rest.dto;

import java.util.List;

public record ListCustomerDTO(
        String id,
        String name,
        String phone,
        List<AppointmentDTO> appointments
) {
}
