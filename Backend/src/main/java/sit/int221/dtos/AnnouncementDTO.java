package sit.int221.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.entities.Category;
import sit.int221.utils.AnnouncementDisplay;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {
    private Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private AnnouncementDisplay announcementDisplay;
    private Integer viewCount;

    @JsonIgnore
    private Category category;
    public String getAnnouncementCategory() {
        return category.getCategoryName();
    }

    @JsonIgnore
    private UserDTO announcementOwner;

    @JsonProperty("announcementOwner")
    public String getAnnouncementOwner() {
        return announcementOwner.getUsername();
    }
}
