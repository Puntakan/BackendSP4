package sit.int221.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.dtos.CreateAndUpdateAnnouncementDTO;
import sit.int221.entities.Announcement;
import sit.int221.entities.Category;
import sit.int221.entities.User;
import sit.int221.exceptions.AnnouncementForbidden;
import sit.int221.exceptions.AnnouncementNotFoundException;
import sit.int221.repositories.AnnouncementRepository;
import sit.int221.utils.AnnouncementDisplay;
import sit.int221.utils.UserRole;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final CategoryService categoryService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public List<Announcement> getAllAnnouncement(String mode, String authorizationHeader) {
        if (authorizationHeader == null) {
            if (Objects.equals(mode, "active")) {
                List<Announcement> activeAnnouncements = announcementRepository.getActiveAnnouncements(AnnouncementDisplay.Y, ZonedDateTime.now());
                activeAnnouncements.sort(Comparator.comparing(Announcement::getId).reversed());
                return activeAnnouncements;
            } else if (Objects.equals(mode, "close")){
                List<Announcement> closeAnnouncements = announcementRepository.findAllByCloseDateIsNotNullAndAnnouncementDisplayEqualsAndCloseDateBefore(AnnouncementDisplay.Y, ZonedDateTime.now());
                closeAnnouncements.sort(Comparator.comparing(Announcement::getId).reversed());
                return closeAnnouncements;
            }
        } else {
            String jwt = authorizationHeader.substring(7);
            UserRole role = UserRole.valueOf(jwtService.extractRole(jwt));
            Integer userId = jwtService.extractUserId(jwt);

            if (role.equals(UserRole.admin)) {
                Sort sort = Sort.by(Sort.Direction.DESC, "id");
                return announcementRepository.findAll(sort);
            } else {
                User user = userService.getUser(userId);
                List<Announcement> announcements = announcementRepository.findAllByAnnouncementOwner(user);
                announcements.sort(Comparator.comparing(Announcement::getId).reversed());
                return announcements;
            }
        }

        return null;
    }

    public Announcement getAnnouncement(Integer announcementId, Boolean viewCount, String authorizationHeader) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new AnnouncementNotFoundException(announcementId));

        if (authorizationHeader != null) {
            String jwt = authorizationHeader.substring(7);
            UserRole role = UserRole.valueOf(jwtService.extractRole(jwt));

            if (role.equals(UserRole.announcer)) {
                User user = userService.getUserByAuthorizationHeader(authorizationHeader);
                if (!Objects.equals(announcement.getAnnouncementOwner().getId(), user.getId())) {
                    throw new AnnouncementForbidden("You are not the owner of this announcement");
                }
            }
        } else {
            if (viewCount != null && viewCount) {
                announcement.setViewCount(announcement.getViewCount() + 1);
                return announcementRepository.saveAndFlush(announcement);
            }
        }

        return announcement;
    }

    public Announcement createAnnouncement(CreateAndUpdateAnnouncementDTO announcement, String authorizationHeader) {
        Category category = categoryService.getCategory(announcement.getCategoryId());
        User user = userService.getUserByAuthorizationHeader(authorizationHeader);
        Announcement newAnnouncement = modelMapper.map(announcement, Announcement.class);
        newAnnouncement.setCategory(category);
        newAnnouncement.setViewCount(0);
        newAnnouncement.setAnnouncementOwner(user);
        return announcementRepository.saveAndFlush(newAnnouncement);
    }

    public Announcement updateAnnouncement(Integer announcementId, CreateAndUpdateAnnouncementDTO announcement, String authorizationHeader) {
        User user = userService.getUserByAuthorizationHeader(authorizationHeader);
        Announcement oldAnnouncement = announcementRepository.findById(announcementId).orElseThrow(() -> new AnnouncementNotFoundException(announcementId));

        if (!Objects.equals(oldAnnouncement.getAnnouncementOwner().getId(), user.getId()) && !Objects.equals(user.getRole(), UserRole.admin)) {
            throw new AnnouncementForbidden("You are not the owner of this announcement");
        }

        Category category = categoryService.getCategory(announcement.getCategoryId());
        oldAnnouncement.setAnnouncementTitle(announcement.getAnnouncementTitle());
        oldAnnouncement.setAnnouncementDescription(announcement.getAnnouncementDescription());
        oldAnnouncement.setCategory(category);
        oldAnnouncement.setPublishDate(announcement.getPublishDate());
        oldAnnouncement.setCloseDate(announcement.getCloseDate());
        oldAnnouncement.setAnnouncementDisplay(announcement.getAnnouncementDisplay());
        return announcementRepository.saveAndFlush(oldAnnouncement);
    }

    public void deleteAnnouncement(Integer announcementId, String authorizationHeader) {
        User user = userService.getUserByAuthorizationHeader(authorizationHeader);
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new AnnouncementNotFoundException(announcementId));

        if (!Objects.equals(announcement.getAnnouncementOwner().getId(), user.getId()) && !Objects.equals(user.getRole(), UserRole.admin)) {
            throw new AnnouncementForbidden("You are not the owner of this announcement");
        }

        announcementRepository.delete(announcement);
    }

    public Page<Announcement> getAnnouncementsByPage(String mode, Integer page, Integer size, Integer categoryId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        ZonedDateTime currentTime = ZonedDateTime.now();

        if (categoryId == 0) {
            if (Objects.equals(mode, "active")) {
                return announcementRepository.getActiveAnnouncementsPage(AnnouncementDisplay.Y, currentTime, pageable);
            } else if (Objects.equals(mode, "close")) {
                return announcementRepository.findAllByCloseDateIsNotNullAndAnnouncementDisplayEqualsAndCloseDateBefore(AnnouncementDisplay.Y, currentTime, pageable);
            } else {
                return announcementRepository.findAll(pageable);
            }
        } else {
            if (Objects.equals(mode, "active")) {
                return announcementRepository.getActiveAnnouncementsPageWithCategoryId(AnnouncementDisplay.Y, currentTime, categoryId, pageable);
            } else if (Objects.equals(mode, "close")) {
                return announcementRepository.findAllByCloseDateIsNotNullAndAnnouncementDisplayEqualsAndCloseDateBeforeAndCategory_Id(AnnouncementDisplay.Y, currentTime, categoryId, pageable);
            } else {
                return announcementRepository.findAllByCategory_Id(categoryId, pageable);
            }
        }
    }
    public List<Announcement> getViewerAllAnnouncement() {
        List<Announcement> announces = announcementRepository.findAll(Sort.by(Sort.Direction.DESC, "publishDate", "closeDate"));
        return announces;
    }

    public Announcement getViewerIdAnnounce(Integer id) {
        return announcementRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement id" + id + " does not exist!!!"));
    }
}
