package ch.usi.inf.bsc.sa4.lab02spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

/**
 * Configures the web security for the application, defining how requests are secured and how the
 * system handles login processes. This configuration sets up the necessary security constraints and
 * integrates custom filter for user creation based on OAuth2 authentication details.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Value("${simurbs.frontend.url}")
  private String frontendUrl;

  /**
   * Defines the security filter chain that specifies CSRF protection, custom filters, authorization
   * rules, and the OAuth2 login configuration. This method sets up the security rules for handling
   * different types of web requests and configures how users are authenticated and authorized in
   * the application.
   *
   * @param http the HttpSecurity object provided by Spring Security to configure components
   * @return a configured SecurityFilterChain that enforces the specified security settings
   * @throws Exception if there is any error in configuring the security settings
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, UserCreationFilter userCreationFilter)
      throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(userCreationFilter, SecurityContextHolderFilter.class)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/users/check-authentication")
                    .permitAll()
                    .requestMatchers("/simulations/public/{id}")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(loginConfigurer -> loginConfigurer.defaultSuccessUrl(frontendUrl))
        .build();
  }
}
