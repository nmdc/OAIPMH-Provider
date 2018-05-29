package no.nmdc.oaipmh.provider.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * Implementation of the MetadataService interface that gives access to DIF
 * metadata records referenced in the properties configuration file
 *
 * @author sjurl
 */
@Service(value = "metadataService")
public class DIFMetadataServiceImpl implements MetadataService {

    @Autowired()
    @Qualifier("providerConf")
    private PropertiesConfiguration configuration;
    
    @Autowired
    JAXBContext difJaxbContext;
    
    private static final Logger LOG = LoggerFactory.getLogger(DIFMetadataServiceImpl.class);
    
    @Override
    public List<DIF> getDifRecords() throws IOException {
        Resource metadatadir = new FileSystemResource(configuration.getString("metadata.folder"));
        File dir = metadatadir.getFile();
    

       List<DIF> difFiles = new ArrayList<>();
   

        
        File[] files = dir.listFiles();
        for (File file : files) {
            try {
                Resource resource = new FileSystemResource(file);
         
                Unmarshaller unmarshaller = difJaxbContext.createUnmarshaller();
		long start = System.currentTimeMillis();
                DIF dif = (DIF) unmarshaller.unmarshal(resource.getInputStream());
		LOG.debug("Parsed: " + file.getAbsolutePath()+" in "+(System.currentTimeMillis()-start)+" ms");

		
                difFiles.add(dif);
            } catch (JAXBException ex) {
                LoggerFactory.getLogger(DIFMetadataServiceImpl.class).error("Unable to unmarshal metadata file: " + file.getAbsolutePath());
            }

        }

        return difFiles;
    }

}
