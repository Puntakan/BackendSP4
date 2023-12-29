package sit.int221.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.utils.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    @NotBlank
    @Size(max = 45, message = "size must be between 1 and 45")
    private String username;

    @NotBlank
    @Size(max = 100, message = "size must be between 1 and 100")
    private String name;

    @NotBlank
    @Size(max = 150, message = "size must be between 1 and 150")
    @Email(message = "Email should be valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
