package id.putra.core_erp.shared.security;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.vaadin.flow.spring.security.AuthenticationContext;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public Optional<AuthenticatedUser> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        String name = "User";
        String email = "";
        String picture = "";

        if (principal instanceof OidcUser oidcUser) {
            name = oidcUser.getFullName() != null ? oidcUser.getFullName()
                    : (oidcUser.getGivenName() != null ? oidcUser.getGivenName() : oidcUser.getName());
            email = oidcUser.getEmail() != null ? oidcUser.getEmail() : "";
            picture = oidcUser.getPicture() != null ? oidcUser.getPicture() : "";
        } else if (principal instanceof OAuth2User oauth2User) {
            name = oauth2User.getAttribute("name") != null ? oauth2User.getAttribute("name") : oauth2User.getName();
            email = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email") : "";
            picture = oauth2User.getAttribute("picture") != null ? oauth2User.getAttribute("picture") : "";
        } else if (principal != null) {
            name = principal.toString();
        }

        if (name == null || name.isBlank()) {
            name = (email != null && !email.isBlank()) ? email : "User";
        }

        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableSet());

        return Optional.of(new AuthenticatedUser(
                name,
                email != null ? email : "",
                picture != null ? picture : "",
                authorities));
    }

    public boolean hasRole(String role) {
        String roleName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase(roleName));
    }

    public boolean isAuthenticated() {
        return authenticationContext.isAuthenticated();
    }

    public void logout() {
        authenticationContext.logout();
    }

    public record AuthenticatedUser(
            String name,
            String email,
            String pictureUrl,
            Set<String> roles) {

        public Set<String> roles() {
            return roles != null ? roles : Collections.emptySet();
        }
    }
}
