package id.putra.core_erp.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AppAuditingAwareUser implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // return Optional.ofNullable(SecurityContextHolder.getContext())
        // .map(SecurityContext::getAuthentication)
        // .filter(Authentication::isAuthenticated)
        // .map(Authentication::getPrincipal)
        // .map(User.class::cast)
        // .map(User::getUsername);
        return Optional.empty();
    }

}
