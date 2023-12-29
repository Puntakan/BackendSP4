package sit.int221.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.AuthenticationRequestDTO;
import sit.int221.dtos.AuthenticationResponseDTO;
import sit.int221.services.AuthenticationService;

@RestController
@RequestMapping("/api/token")
@CrossOrigin(origins= {"http://localhost:5173", "https://intproj22.sit.kmutt.ac.th"})
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public AuthenticationResponseDTO authenticate(@RequestBody AuthenticationRequestDTO request) {
        return authenticationService.authenticate(request);
    }

    @GetMapping
    public AuthenticationResponseDTO refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.refreshToken(authorizationHeader);
    }
}
