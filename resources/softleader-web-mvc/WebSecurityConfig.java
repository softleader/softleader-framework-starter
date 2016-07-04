package {pkg}.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


import {pkg}.service.UserDetailsService;
import tw.com.softleader.security.authentication.MoreDetailsBinder;
import tw.com.softleader.security.authentication.MoreUserDetailsService;
import tw.com.softleader.security.config.MoreWebSecurityConfiguration;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends MoreWebSecurityConfiguration {

  @Bean
  @Override
  public MoreDetailsBinder moreDetailsBinder() {
    return (request, details) -> {
      String channelCode = request.getParameter("channelCode");
      details.getMore().put("channelCode", channelCode);
    };
  }

  @Bean
  @Override
  public MoreUserDetailsService moreUserDetailsService() {
    return new UserDetailsService();
  }

}
