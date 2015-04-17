package no.nmdc.oaipmh.provider.config;

import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuration class that enables mvc and sets up the xml conversion for
 * responses and provides the metadata from configuration files
 *
 * @author sjurl
 */
@Configuration
@EnableWebMvc
public class ApplicationConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jaxb2RootElementHttpMessageConverter j = new Jaxb2RootElementHttpMessageConverter();
        converters.add(new Jaxb2RootElementHttpMessageConverter());
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false).
                favorParameter(true).
                ignoreAcceptHeader(true).
                parameterName("format").
                useJaf(false).
                defaultContentType(MediaType.APPLICATION_XML).
                mediaType("xml", MediaType.APPLICATION_XML);
    }

    @Bean(name = "providerConf")
    public PropertiesConfiguration getProviderConfiguration() throws ConfigurationException {
        PropertiesConfiguration conf = new PropertiesConfiguration(System.getProperty("catalina.base") + "/conf/oaipmh-provider.properties");
        conf.setReloadingStrategy(new FileChangedReloadingStrategy());
        return conf;
    }
}
