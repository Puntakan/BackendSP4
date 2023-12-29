package sit.int221.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.utils.AnnouncementDisplay;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "announces")
public class Announcement {
    @Id
    @Column(name = "announcementId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private Integer viewCount;

    @Enumerated(EnumType.STRING)
    private AnnouncementDisplay announcementDisplay = AnnouncementDisplay.N;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "announcementOwner")
    private User announcementOwner;
}
