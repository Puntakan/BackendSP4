package sit.int221.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int221.dtos.CreateUserDTO;
import sit.int221.dtos.UpdateUserDTO;
import sit.int221.dtos.UserMatchDTO;
import sit.int221.entities.Announcement;
import sit.int221.entities.User;
import sit.int221.exceptions.*;
import sit.int221.repositories.AnnouncementRepository;
import sit.int221.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AnnouncementRepository announcementRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser(){
        Sort sortRoleAndUsername = Sort.by(Sort.Direction.ASC, "role", "username");
        return userRepository.findAll(sortRoleAndUsername);
    }

    public User getUser(Integer userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User createUser(CreateUserDTO user){
        String trimmedName = user.getName().trim();
        String trimmedUsername = user.getUsername().trim();
        String trimmedEmail = user.getEmail().trim();
        String trimmedPassword = user.getPassword().trim();

        String hashedPassword = passwordEncoder.encode(trimmedPassword);

        User newUser = modelMapper.map(user, User.class);
        newUser.setUsername(trimmedUsername);
        newUser.setName(trimmedName);
        newUser.setEmail(trimmedEmail);
        newUser.setPassword(hashedPassword);
        return userRepository.saveAndFlush(newUser);
    }

    public User updateUser(Integer userId, UpdateUserDTO userDetail){
        String trimmedName = userDetail.getName().trim();
        String trimmedUsername = userDetail.getUsername().trim();
        String trimmedEmail = userDetail.getEmail().trim();
        User existUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        validateUniqueOnUpdate(existUser, trimmedUsername, trimmedName, trimmedEmail);

        existUser.setName(trimmedName);
        existUser.setUsername(trimmedUsername);
        existUser.setEmail(trimmedEmail);
        existUser.setRole(userDetail.getRole());
        return userRepository.saveAndFlush(existUser);
    }

    private void validateUniqueOnUpdate(User existUser, String trimmedUsername, String trimmedName, String trimmedEmail){
        List<String> fieldError = new ArrayList<>();

        if (!trimmedUsername.equalsIgnoreCase(existUser.getUsername())) {
            Optional<User> duplicateUsername = userRepository.findByUsername(trimmedUsername);
            if (duplicateUsername.isPresent()){
                fieldError.add("username");
            }
        }

        if (!trimmedName.equalsIgnoreCase(existUser.getName())){
            User duplicateName = userRepository.findByName(trimmedName);
            if (duplicateName != null){
                fieldError.add("name");
            }
        }

        if (!trimmedEmail.equalsIgnoreCase(existUser.getEmail())){
            User duplicateEmail = userRepository.findByEmail(trimmedEmail);
            if (duplicateEmail != null){
                fieldError.add("email");
            }
        }

        if (!fieldError.isEmpty()){
            throw new NotUniqueOnUpdateException(fieldError);
        }
    }

    public void deleteUser(Integer id, String authorizationHeader) {
        User existUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User user = getUserByAuthorizationHeader(authorizationHeader);
        if (user.getId() == existUser.getId()){
            throw new RuntimeException("You cannot delete your own account");
        }
        List<Announcement> announcements = announcementRepository.findAllByAnnouncementOwner(existUser);
        for (Announcement announcement : announcements){
            announcement.setAnnouncementOwner(user);
        }
        userRepository.delete(existUser);
    }

    public boolean checkMatch(UserMatchDTO userMatchDTO ) {
        Optional<User> user = userRepository.findByUsername(userMatchDTO.getUsername());

        if (user.isEmpty()) {
            throw new UserNotFoundException(userMatchDTO.getUsername());
        } else if (passwordEncoder.matches(userMatchDTO.getPassword(), user.get().getPassword())) {
            return true;
        } else {
            throw new UnauthorizedException("Password is not match");
        }
    }

    public User getUserByAuthorizationHeader(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Integer userId = jwtService.extractUserId(token);
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
