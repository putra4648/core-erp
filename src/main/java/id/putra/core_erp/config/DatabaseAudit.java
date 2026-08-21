package id.putra.core_erp.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class DatabaseAudit {
    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                OidcUser jwt = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                return java.util.Objects.requireNonNull(Optional.ofNullable(jwt.getSubject()));
            }
        };
    }
}
