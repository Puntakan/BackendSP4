package sit.int221.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.utils.UserRole;
import sit.int221.validators.ValidUniqueOnCreate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidUniqueOnCreate
public class CreateUserDTO {
    @NotBlank
    @Size(max = 45, message = "size must be between 1 and 45")
    private String username;

    @NotBlank
    @Size(min = 8, max = 14, message = "size must be between 8 and 14")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).+$",
            message = "must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters")
    private String password;

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
