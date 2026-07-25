package com.cesarvillarreal.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

/**
 * Spring Security is present for CSRF protection and security response headers only.
 * <p>
 * There is no authentication anywhere in this application: every route is {@code permitAll()},
 * including the actuator health endpoint that Render's deploy healthcheck depends on. A chain
 * that accidentally requires authentication for {@code /actuator/health} would fail every
 * subsequent deploy, not just the current one.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Content-Security-Policy, sent in <strong>Report-Only</strong> mode.
     * <p>
     * Tuned against what the templates actually load:
     * <ul>
     *   <li>{@code script-src} — the Tailwind CDN, which compiles utility classes at runtime and
     *       therefore needs {@code 'unsafe-eval'}; plus {@code 'unsafe-inline'} for the theme-init,
     *       tailwind.config, and menu/dark-mode blocks in {@code layout/base.html} and the
     *       JSON-LD block in {@code fragments/json-ld.html}.</li>
     *   <li>{@code style-src} — the Google Fonts stylesheet plus the inline {@code <style>} blocks.</li>
     *   <li>{@code font-src} — Google's font files.</li>
     *   <li>{@code img-src} — {@code data:} covers inline SVG/PNG data URIs.</li>
     * </ul>
     * Dropping the Tailwind CDN (and with it {@code 'unsafe-inline'}/{@code 'unsafe-eval'}) is the
     * prerequisite for a genuinely strict policy; {@code .claude/rules/code-style.md} currently
     * forbids installing Tailwind locally.
     */
    private static final String CONTENT_SECURITY_POLICY = String.join("; ",
            "default-src 'self'",
            "script-src 'self' https://cdn.tailwindcss.com 'unsafe-inline' 'unsafe-eval'",
            "style-src 'self' https://fonts.googleapis.com 'unsafe-inline'",
            "font-src 'self' https://fonts.gstatic.com data:",
            "img-src 'self' data:",
            "connect-src 'self'",
            "form-action 'self'",
            "base-uri 'self'",
            "object-src 'none'",
            "frame-ancestors 'none'");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // No route is protected. Security is here for CSRF and headers, not access control.
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())

                // CSRF stays enabled (the Spring Security default). Thymeleaf injects the token
                // into any form using th:action, which is how POST /contact keeps working.

                .headers(headers -> headers
                        .contentTypeOptions(Customizer.withDefaults())
                        .frameOptions(frameOptions -> frameOptions.deny())
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000))
                        // reportOnly() is what emits Content-Security-Policy-Report-Only rather
                        // than the enforcing header. It cannot break the page. Flipping this to
                        // enforcing is a later phase, once the console is verified clean.
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(CONTENT_SECURITY_POLICY)
                                .reportOnly())

                        // Spring Security otherwise stamps `Cache-Control: no-store` on every
                        // response, including images, CSS and fonts. This site serves no
                        // authenticated content, so that only costs browser and Cloudflare
                        // caching. Leave caching to Boot's own static-resource configuration.
                        .cacheControl(cache -> cache.disable())

                        // Kept last: in Spring Security 6.2 permissionsPolicy() returns the
                        // PermissionsPolicyConfig rather than the HeadersConfigurer, so nothing
                        // can be chained after it.
                        .permissionsPolicy(permissions -> permissions
                                .policy("geolocation=(), microphone=(), camera=()")));

        return http.build();
    }
}
