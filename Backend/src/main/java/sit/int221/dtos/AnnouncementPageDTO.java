package sit.int221.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
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
public class AnnouncementPageDTO {
    @Id
    private Integer id;
    private String announcementTitle;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private AnnouncementDisplay announcementDisplay;
    private Integer viewCount;

    @JsonIgnore
    private Category category;
    public String getAnnouncementCategory() {
        return category.getCategoryName();
    }
}
