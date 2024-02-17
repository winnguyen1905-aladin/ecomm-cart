package winnguyen1905.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignClientConfig {
  @Bean
  public RequestInterceptor requestTokenBearerInterceptor() {
    return new RequestInterceptor() {
      @Override
      public void apply(RequestTemplate requestTemplate) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (token != null) {
          requestTemplate.header("Authorization", "Bearer " + token.getToken().getTokenValue());
        }
      }
    };
  }
}
