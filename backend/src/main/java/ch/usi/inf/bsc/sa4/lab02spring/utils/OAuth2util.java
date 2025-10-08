package ch.usi.inf.bsc.sa4.lab02spring.utils;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

/** util class for the authentication */
public final class OAuth2util {

  /**
   * retrieves the id of a specific user
   *
   * @param authentication authentication token of a user
   * @return the id of the specific user contained in this authentication token
   */
  public static String getUserID(OAuth2AuthenticationToken authentication) {
    return authentication.getPrincipal().getAttribute("sub");
  }
}
