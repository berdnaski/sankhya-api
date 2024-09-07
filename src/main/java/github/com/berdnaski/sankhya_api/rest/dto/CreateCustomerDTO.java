package github.com.berdnaski.sankhya_api.rest.dto;

public record CreateCustomerDTO(
        String id,
        String name,
        String phone,
        String password
) {
}
