package no.nmdc.oaipmh.provider.config;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.RecordType;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Terry Hannant <a5119>
 */
@Configuration
public class JaxbConfig {
    
    @Bean
     JAXBContext recordTypeJaxbContext() throws JAXBException {
        return   JAXBContext.newInstance(RecordType.class);
        
    }

     @Bean
     JAXBContext difJaxbContext() throws JAXBException {
        return   JAXBContext.newInstance(DIF.class);
        
    }

     @Bean
     JAXBContext oaiJaxbContext() throws JAXBException {
        return    JAXBContext.newInstance(OAIPMHtype.class);
        
    }


}
