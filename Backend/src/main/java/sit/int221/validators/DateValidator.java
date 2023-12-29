package sit.int221.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import sit.int221.dtos.CreateAndUpdateAnnouncementDTO;

@Component
public class DateValidator implements ConstraintValidator<ValidDate, CreateAndUpdateAnnouncementDTO> {
    @Override
    public boolean isValid(CreateAndUpdateAnnouncementDTO createAndUpdateAnnouncementDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (createAndUpdateAnnouncementDTO.getCloseDate() == null || createAndUpdateAnnouncementDTO.getPublishDate() == null) {
            return true;
        }

        boolean isCloseDateAfterPublishDate = createAndUpdateAnnouncementDTO.getCloseDate().isAfter(createAndUpdateAnnouncementDTO.getPublishDate());

        if (!isCloseDateAfterPublishDate) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("closeDate")
                    .addConstraintViolation();
        }

        return isCloseDateAfterPublishDate;
    }
}
