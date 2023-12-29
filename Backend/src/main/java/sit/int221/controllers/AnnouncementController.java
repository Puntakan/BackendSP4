package sit.int221.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.*;
import sit.int221.entities.Announcement;
import sit.int221.services.AnnouncementService;
import sit.int221.utils.ListMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @GetMapping("")
    public List<AnnouncementDTO> getAllAnnouncement(@RequestParam(required = false) String mode,
                                                    @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        List<Announcement> announcements = announcementService.getAllAnnouncement(mode, authorizationHeader);
        List<AnnouncementDTO> announcementDTOs = announcements.stream()
                .map(announcement -> modelMapper.map(announcement, AnnouncementDTO.class))
                .collect(Collectors.toList());

        return announcementDTOs;
    }

    @GetMapping("/{id}")
    public AnnouncementDTO getAnnouncement(@PathVariable Integer id,
                                           @RequestParam(required = false) Boolean count,
                                           @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Announcement announcement = announcementService.getAnnouncement(id, count, authorizationHeader);
        return modelMapper.map(announcement, AnnouncementDTO.class);
    }

    @PostMapping("")
    public CreateAndUpdateAnnouncementResponseDTO createAnnouncement(@Valid @RequestBody CreateAndUpdateAnnouncementDTO newAnnouncement,
                                                                     @RequestHeader(value = "Authorization") String authorizationHeader,
                                                                     BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        }

        Announcement announcement = announcementService.createAnnouncement(newAnnouncement, authorizationHeader);
        return modelMapper.map(announcement, CreateAndUpdateAnnouncementResponseDTO.class);
    }

    @PutMapping("/{id}")
    public CreateAndUpdateAnnouncementResponseDTO updateAnnouncement(@PathVariable Integer id,
                                                                     @Valid @RequestBody CreateAndUpdateAnnouncementDTO newAnnouncement,
                                                                     @RequestHeader(value = "Authorization") String authorizationHeader,
                                                                     BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        }

        Announcement announcement = announcementService.updateAnnouncement(id, newAnnouncement, authorizationHeader);
        return modelMapper.map(announcement, CreateAndUpdateAnnouncementResponseDTO.class);
    }

    @DeleteMapping("/{id}")
    public void deleteAnnouncement(@PathVariable Integer id, @RequestHeader(value = "Authorization") String authorizationHeader) {
        announcementService.deleteAnnouncement(id, authorizationHeader);
    }

    @GetMapping("/viewer")
    public List<AnnouncementDTO> getViewerAllAnnouncement() {
        List<Announcement> announcements = announcementService.getViewerAllAnnouncement();
        List<AnnouncementDTO> announcementDTOs = announcements.stream()
                .map(announcement -> modelMapper.map(announcement, AnnouncementDTO.class))
                .collect(Collectors.toList());

        return announcementDTOs;
    }

    @GetMapping("/viewer/{id}")
    public AnnouncementDTO getViewerIdAnnounce(@PathVariable Integer id){
        return modelMapper.map(announcementService.getViewerIdAnnounce(id), AnnouncementDTO.class);
    }

}
