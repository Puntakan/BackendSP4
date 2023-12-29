package sit.int221.services;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sit.int221.dtos.AuthenticationRequestDTO;
import sit.int221.dtos.AuthenticationResponseDTO;
import sit.int221.entities.User;
import sit.int221.exceptions.UnauthorizedException;
import sit.int221.exceptions.UserNotFoundException;
import sit.int221.repositories.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        try {
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UserNotFoundException(request.getUsername()));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            Map<String, Object> extraClaims = Map.of("userId", user.getId(),"role", user.getRole());
            String token = jwtService.generateToken(extraClaims, user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return new AuthenticationResponseDTO(token, refreshToken);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Password does not match for username: " + request.getUsername());
        }
    }

    public AuthenticationResponseDTO refreshToken(String authorizationHeader) {
        final String refreshToken;
        final String username;

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        try {
            refreshToken = authorizationHeader.substring(7);
            username = jwtService.extractUsername(refreshToken);

            if (username != null) {
                User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
                if (jwtService.isTokenValid(refreshToken, user)) {
                    Map<String, Object> extraClaims = Map.of("userId", user.getId(),"role", user.getRole());
                    String token = jwtService.generateToken(extraClaims, user);
                    return new AuthenticationResponseDTO(token, refreshToken);
                }
            }
        } catch (ExpiredJwtException ex) {
            throw new UnauthorizedException("Refresh token has expired");
        }

        throw new UnauthorizedException("Invalid refresh token");
    }
}
