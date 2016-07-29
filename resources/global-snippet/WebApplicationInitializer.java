package {pkg}.config;

import javax.servlet.Filter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebApplicationInitializer
    extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[] {rootConfigClasses};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] {servletConfigClasses};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] {"/"};
  }

}
