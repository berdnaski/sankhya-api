package github.com.berdnaski.sankhya_api.rest.dto;

import java.util.UUID;

public record ResponseDTO(UUID id, String name, String phone, String token) {}
