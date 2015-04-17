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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXParseException;

/**
 *
 * @author sjurl
 */
@Service(value = "metadataService")
public class DIFMetadataServiceImpl implements MetadataService {

    @Autowired()
    @Qualifier("providerConf")
    private PropertiesConfiguration configuration;

    @Override
    public List<DIF> getDifRecords() throws IOException {
        Resource metadatadir = new FileSystemResource(configuration.getString("metadata.folder"));
        File dir = metadatadir.getFile();

        File[] files = dir.listFiles();
        List<DIF> difFiles = new ArrayList<>();
        for (File file : files) {
            try {
                Resource resource = new FileSystemResource(file);
                JAXBContext difcontext = JAXBContext.newInstance(DIF.class);
                Unmarshaller unmarshaller = difcontext.createUnmarshaller();

                DIF dif = (DIF) unmarshaller.unmarshal(resource.getInputStream());
                difFiles.add(dif);
            } catch (JAXBException ex) {
                LoggerFactory.getLogger(DIFMetadataServiceImpl.class).error("Unable to unmarshal metadata file: " + file.getAbsolutePath());
            }

        }
        return difFiles;
    }

}