package sit.int221.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import static org.springframework.security.config.Customizer.withDefaults;
import static sit.int221.utils.Permission.*;
import static sit.int221.utils.UserRole.admin;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private static final String[][] PUBLIC_ENDPOINTS = {
            { HttpMethod.GET.toString(), "/api/announcements/pages" },
            { HttpMethod.GET.toString(), "/api/announcements/**" },
            { HttpMethod.GET.toString(), "/api/category" },
            { HttpMethod.POST.toString(), "/api/token" },
    };

    public static boolean isPublicEndpoint(String method, String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String[] endpoint : PUBLIC_ENDPOINTS) {
            if (antPathMatcher.match(endpoint[1], path) && endpoint[0].equals(method)) {
                return true;
            }
        }
        return false;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                            for (String[] endpoint : PUBLIC_ENDPOINTS) {
                                String httpMethod = endpoint[0];
                                String endpointPath = endpoint[1];
                                request.requestMatchers(httpMethod, endpointPath).permitAll();
                            }

                            request
                                    .requestMatchers("/api/users/**").hasRole(admin.name())
                                    .requestMatchers("/api/match/**").hasRole(admin.name())
                                    .requestMatchers(HttpMethod.GET, "/api/users/**").hasAuthority(ADMIN_READ.name())
                                    .requestMatchers(HttpMethod.POST, "/api/users/**").hasAuthority(ADMIN_CREATE.name())
                                    .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAuthority(ADMIN_UPDATE.name())
                                    .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority(ADMIN_DELETE.name())
                                    .requestMatchers(HttpMethod.POST, "/api/match/**").hasAuthority(ADMIN_CREATE.name());

                            request.anyRequest().authenticated();
                        }
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
