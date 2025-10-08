package ch.usi.inf.bsc.sa4.lab02spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class that implements WebMvcConfigurer. This class is used to set up
 * web-specific configurations for the application, including CORS (Cross-Origin Resource Sharing)
 * settings.
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
  /** Instatiated value of the frontend url used for deployment */
  @Value("${simurbs.frontend.url}")
  private String frontendUrl;

  /**
   * Configures CORS settings for the entire application. This method specifies the allowed origins,
   * HTTP methods, and credentials settings for cross-origin requests.
   *
   * @param registry the CorsRegistry to which CORS mappings are added.
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    final CorsRegistration registration = registry.addMapping("/**");
    registration.allowedOrigins(frontendUrl);
    registration.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    registration.allowCredentials(true);
  }
}
