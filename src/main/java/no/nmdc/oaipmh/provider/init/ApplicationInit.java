package no.nmdc.oaipmh.provider.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 * Application initalization. This allows us to init without the web.xml file.
 *
 * @author kjetilf
 */
public class ApplicationInit extends AbstractDispatcherServletInitializer {


    @Override
    protected String[] getServletMappings() {
        return new String[]{"/request/*"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext cxt = new AnnotationConfigWebApplicationContext();
        cxt.scan("no.nmdc.oaipmh.provider.config", "no.nmdc.oaipmh.provider.controller", "no.nmdc.oaipmh.provider.service");
        return cxt;
    }

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext cxt = new AnnotationConfigWebApplicationContext();
        return cxt;
    }
}