package id.putra.core_erp.shared.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}")
    private String issuer;

    @Value("${spring.security.oauth2.client.registration.auth0.client-id}")
    private String clientId;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**", "/images/**", "/line-awesome/**", "/icons/**").permitAll());

        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            configurer.oauth2LoginPage("/login");
            configurer.addLogoutHandler(logoutHandler());
            configurer.anyRequest(auth -> auth.authenticated());
        });

        return http.build();
    }

    private LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            try {
                String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                String cleanIssuer = issuer.endsWith("/") ? issuer : issuer + "/";
                String logoutUrl = cleanIssuer + "v2/logout?client_id=" + clientId + "&returnTo=" + baseUrl;
                response.sendRedirect(logoutUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to redirect to Auth0 logout", e);
            }
        };
    }

    @Bean
    GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                mappedAuthorities.add(authority);

                // Ekstrak custom claim roles dari Auth0 OIDC Token
                if (authority instanceof OidcUserAuthority oidcAuth) {
                    log.info("OidcAuth: {}", oidcAuth.getIdToken().getClaims());
                    List<String> roles = oidcAuth.getIdToken()
                            .getClaimAsStringList("https://putracoderz.my.id/erp/roles");
                    if (roles != null) {
                        for (String role : roles) {
                            // Format menjadi standard Spring Security: ROLE_ADMIN, ROLE_USER
                            mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                        }
                    }
                }
            });
            log.info("Mapped authorities: {}", mappedAuthorities);

            return mappedAuthorities;
        };
    }
}
