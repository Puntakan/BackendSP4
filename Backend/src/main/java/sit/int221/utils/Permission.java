package sit.int221.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    ANNOUNCER_READ("announcer:read"),
    ANNOUNCER_UPDATE("announcer:update"),
    ANNOUNCER_CREATE("announcer:create"),
    ANNOUNCER_DELETE("announcer:delete");

    private final String permission;
}
