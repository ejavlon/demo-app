package uz.ejavlon.demoapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String firstName;

    private String lastName;

    @NotNull(message = "username empty")
    private String username;

    @NotNull(message = "password empty")
    private String password;
}
