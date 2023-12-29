package sit.int221.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static sit.int221.utils.Permission.*;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    admin(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    ANNOUNCER_READ,
                    ANNOUNCER_UPDATE,
                    ANNOUNCER_DELETE,
                    ANNOUNCER_CREATE
            )
    ),
    announcer(
            Set.of(
                    ANNOUNCER_READ,
                    ANNOUNCER_UPDATE,
                    ANNOUNCER_DELETE,
                    ANNOUNCER_CREATE
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
