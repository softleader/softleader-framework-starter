package {pkg}.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import tw.com.softleader.data.config.DataSourceConfiguration;
import tw.com.softleader.domain.config.DomainConfiguration;
import tw.com.softleader.web.mvc.config.WebMvcConfiguration;

public class WebApplicationInitializer
    extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[] {DataSourceConfiguration.class, DomainConfiguration.class, WebSecurityConfig.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] {WebMvcConfiguration.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] {"/"};
  }

}
