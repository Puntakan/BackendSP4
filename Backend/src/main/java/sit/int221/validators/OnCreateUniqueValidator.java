package sit.int221.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sit.int221.dtos.CreateUserDTO;
import sit.int221.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class OnCreateUniqueValidator implements ConstraintValidator<ValidUniqueOnCreate, CreateUserDTO> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(CreateUserDTO user, ConstraintValidatorContext constraintValidatorContext) {
        boolean isUsernameAlreadyExist = userRepository.countByUsername(user.getUsername()) > 0;
        boolean isEmailAlreadyExist = userRepository.countByEmail(user.getEmail()) > 0;
        boolean isNameAlreadyExist = userRepository.countByName(user.getName()) > 0;
        boolean isValid = true;

        if (isUsernameAlreadyExist) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())                    .addPropertyNode("username")
                    .addConstraintViolation();
            isValid = false;
        }

        if (isEmailAlreadyExist) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        if (isNameAlreadyExist) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}