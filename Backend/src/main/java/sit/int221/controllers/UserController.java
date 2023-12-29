package sit.int221.controllers;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.CreateUserDTO;
import sit.int221.dtos.UpdateUserDTO;
import sit.int221.dtos.UserDTO;
import sit.int221.dtos.UserMatchDTO;
import sit.int221.entities.User;
import sit.int221.services.UserService;
import sit.int221.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins= {"http://localhost:5173", "https://intproj22.sit.kmutt.ac.th"})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;
    private final EntityManager entityManager;

    @GetMapping
    public List<UserDTO> getAllUser(){
        List<User> users = userService.getAllUser();
        return listMapper.mapList(users, UserDTO.class, modelMapper);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Integer id){
        User user = userService.getUser(id);
        return modelMapper.map(user, UserDTO.class);
    }

    @PostMapping("")
    @Transactional
    public UserDTO createUser(@Valid @RequestBody CreateUserDTO newUser){
        User user = userService.createUser(newUser);
        entityManager.refresh(user);
        return modelMapper.map(user, UserDTO.class);
    }

    @PutMapping("/{userId}")
    @Transactional
    public UserDTO updateUser(@PathVariable Integer userId, @Valid @RequestBody UpdateUserDTO userDetail) {
        User user = userService.updateUser(userId,userDetail);
        entityManager.refresh(user);

        return modelMapper.map(user, UserDTO.class);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id, @RequestHeader(value = "Authorization") String authorizationHeader) {
        userService.deleteUser(id, authorizationHeader);
    }

    @PostMapping("/match")
    public boolean checkMatch(@RequestBody UserMatchDTO userMatchDTO){
        return userService.checkMatch(userMatchDTO);
    }
}
