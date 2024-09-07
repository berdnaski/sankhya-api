package github.com.berdnaski.sankhya_api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhoneInfoDTO {
    private String name;
    private String phone;
    private String password;
}
