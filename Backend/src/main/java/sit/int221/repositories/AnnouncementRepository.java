package sit.int221.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import sit.int221.entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.entities.User;
import sit.int221.utils.AnnouncementDisplay;

import java.time.ZonedDateTime;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    // condition for active announcement
    // 1. if publishDate and closeDate are null and announcementDisplay is Y
    // 2. if publishDate is not null and closeDate is null and announcementDisplay is Y and publishDate is before now
    // 3. if publishDate is not null and closeDate is not null and announcementDisplay is Y and publishDate is before now and closeDate is after now
    // 4. if publishDate is null and closeDate is not null and announcementDisplay is Y and closeDate is after now
    @Query("SELECT a FROM Announcement a WHERE " +
            "(a.announcementDisplay = :announcementDisplay AND " +
            "((a.publishDate IS NULL AND a.closeDate IS NULL) OR " +
            "(a.publishDate IS NOT NULL AND a.closeDate IS NULL AND a.publishDate < :currentTime) OR " +
            "(a.publishDate IS NOT NULL AND a.closeDate IS NOT NULL AND a.publishDate < :currentTime AND a.closeDate > :currentTime) OR " +
            "(a.publishDate IS NULL AND a.closeDate IS NOT NULL AND a.closeDate > :currentTime)))")
    List<Announcement> getActiveAnnouncements(AnnouncementDisplay announcementDisplay, ZonedDateTime currentTime);

    // active but page
    @Query("SELECT a FROM Announcement a WHERE " +
            "(a.announcementDisplay = :announcementDisplay AND " +
            "((a.publishDate IS NULL AND a.closeDate IS NULL) OR " +
            "(a.publishDate IS NOT NULL AND a.closeDate IS NULL AND a.publishDate < :currentTime) OR " +
            "(a.publishDate IS NOT NULL AND a.closeDate IS NOT NULL AND a.publishDate < :currentTime AND a.closeDate > :currentTime) OR " +
            "(a.publishDate IS NULL AND a.closeDate IS NOT NULL AND a.closeDate > :currentTime)))")
    Page<Announcement> getActiveAnnouncementsPage(AnnouncementDisplay announcementDisplay, ZonedDateTime currentTime, Pageable pageable);

    // active page but with category id
    @Query("SELECT a FROM Announcement a WHERE " +
            "(a.announcementDisplay = :announcementDisplay AND " +
            "((a.publishDate IS NULL AND a.closeDate IS NULL) OR " +
            "(a.publishDate IS NOT NULL AND a.closeDate IS NULL AND a.publishDate < :currentTime) OR " +
            "(a.publishDate IS NOT NULL AND a.closeDate IS NOT NULL AND a.publishDate < :currentTime AND a.closeDate > :currentTime) OR " +
            "(a.publishDate IS NULL AND a.closeDate IS NOT NULL AND a.closeDate > :currentTime))) AND " +
            "a.category.id = :categoryId")
    Page<Announcement> getActiveAnnouncementsPageWithCategoryId(AnnouncementDisplay announcementDisplay, ZonedDateTime currentTime, Integer categoryId, Pageable pageable);

    // condition for close announcement
    // if closeDate is not null, announcementDisplay is Y and closeDate is before now
    List<Announcement> findAllByCloseDateIsNotNullAndAnnouncementDisplayEqualsAndCloseDateBefore(AnnouncementDisplay announcementDisplay, ZonedDateTime currentTime);
    // close but page
    Page<Announcement> findAllByCloseDateIsNotNullAndAnnouncementDisplayEqualsAndCloseDateBefore(AnnouncementDisplay announcementDisplay, ZonedDateTime currentTime, Pageable pageable);
    // close page but with category id
    Page<Announcement> findAllByCloseDateIsNotNullAndAnnouncementDisplayEqualsAndCloseDateBeforeAndCategory_Id(AnnouncementDisplay announcementDisplay, ZonedDateTime currentTime, Integer categoryId, Pageable pageable);

    Page<Announcement> findAllByCategory_Id(Integer categoryId, Pageable pageable);


    List<Announcement> findAllByAnnouncementOwner(User user);
}
