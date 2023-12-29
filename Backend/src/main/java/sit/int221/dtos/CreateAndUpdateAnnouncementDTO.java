package sit.int221.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.utils.AnnouncementDisplay;
import sit.int221.validators.ValidCategoryId;
import sit.int221.validators.ValidDate;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidDate
public class CreateAndUpdateAnnouncementDTO {
    @NotNull @NotBlank @Size(min = 1, max = 200)
    private String announcementTitle;
    @NotNull @NotBlank @Size(min = 1, max = 10000)
    private String announcementDescription;
    @FutureOrPresent
    private ZonedDateTime publishDate;
    @Future
    private ZonedDateTime closeDate;
    @NotNull(message = "must be either 'Y' or 'N'")
    private AnnouncementDisplay announcementDisplay;
    @NotNull @ValidCategoryId
    private Integer categoryId;
    public void setAnnouncementDisplay(String announcementDisplay) {
        if(Objects.equals(announcementDisplay, "N") || Objects.equals(announcementDisplay, "Y")){
            this.announcementDisplay = AnnouncementDisplay.valueOf(announcementDisplay);
        } else if (announcementDisplay==null) {
            this.announcementDisplay = AnnouncementDisplay.N;
        } else {
            this.announcementDisplay = null;
        }
    }
}
