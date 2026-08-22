package id.putra.core_erp.shared.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
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
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
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

    @Value("${spring.security.oauth2.client.registration.auth0.client-id:}")
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

                Map<String, Object> attributes = null;
                if (authority instanceof OidcUserAuthority oidcAuth) {
                    attributes = oidcAuth.getAttributes();
                } else if (authority instanceof OAuth2UserAuthority oauth2Auth) {
                    attributes = oauth2Auth.getAttributes();
                }

                if (attributes != null) {
                    extractRolesAndPermissions(attributes, mappedAuthorities);
                }
            });
            log.info("Mapped authorities: {}", mappedAuthorities);

            return mappedAuthorities;
        };
    }

    private void extractRolesAndPermissions(Map<String, Object> attributes, Set<GrantedAuthority> mappedAuthorities) {
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String key = entry.getKey().toLowerCase();
            if (key.endsWith("/roles") || key.equals("roles") || key.endsWith("/groups") || key.equals("groups")) {
                Object value = entry.getValue();
                if (value instanceof Collection<?> collection) {
                    for (Object item : collection) {
                        if (item != null) {
                            String role = item.toString().trim().toUpperCase();
                            if (!role.startsWith("ROLE_")) {
                                role = "ROLE_" + role;
                            }
                            mappedAuthorities.add(new SimpleGrantedAuthority(role));
                        }
                    }
                } else if (value instanceof String roleStr) {
                    String role = roleStr.trim().toUpperCase();
                    if (!role.startsWith("ROLE_")) {
                        role = "ROLE_" + role;
                    }
                    mappedAuthorities.add(new SimpleGrantedAuthority(role));
                }
            } else if (key.endsWith("/permissions") || key.equals("permissions")) {
                Object value = entry.getValue();
                if (value instanceof Collection<?> collection) {
                    for (Object item : collection) {
                        if (item != null) {
                            mappedAuthorities.add(new SimpleGrantedAuthority(item.toString().trim()));
                        }
                    }
                }
            }
        }
    }
}
