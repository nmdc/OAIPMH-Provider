package no.nmdc.oaipmh.provider.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import no.nmdc.oaipmh.provider.dao.DatasetDao;
import no.nmdc.oaipmh.provider.dao.dto.Dataset;
import no.nmdc.oaipmh.provider.domain.ListSetsType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import no.nmdc.oaipmh.provider.domain.dif.DataSetCitation;
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

    private static final Logger LOG = LoggerFactory.getLogger(DIFMetadataServiceImpl.class);

    @Autowired()
    @Qualifier("providerConf")
    private PropertiesConfiguration configuration;

    @Autowired
    private DatasetDao datasetDao;

    @Autowired
    JAXBContext difJaxbContext;

    @Override
    public List<DIF> getDifRecords() throws IOException {

        Resource metadatadir = new FileSystemResource(configuration.getString("metadata.folder"));
        File dir = metadatadir.getFile();

        File[] files = dir.listFiles();
        List<DIF> difFiles = new ArrayList<>();
        try {
            Unmarshaller difUnmarshaller = difJaxbContext.createUnmarshaller();
            for (File file : files) {
                try {
                    Resource resource = new FileSystemResource(file);
                    DIF dif = (DIF) difUnmarshaller.unmarshal(resource.getInputStream());
                    difFiles.add(dif);
                    LOG.debug("Parsed: " + file.getAbsolutePath());
                } catch (JAXBException ex) {
                    LOG.error("Unable to unmarshal metadata file: " + file.getAbsolutePath());
                }

            }

        } catch (JAXBException ex) {
            java.util.logging.Logger.getLogger(DIFMetadataServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return difFiles;
    }

    @Override
    public OAIPMHtype getSets(String resumptionToke) {
        OAIPMHtype oaipmh = new OAIPMHtype();
        oaipmh.setListSets(new ListSetsType());
        oaipmh.getListSets().getSet().addAll(datasetDao.getDistinctOriginatingCenters());
        return oaipmh;
    }

    @Override
    public List<DIF> getDifRecords(String set) {

        List<Dataset> datasets = datasetDao.findByOriginatingCenter(set);
        List<DIF> difFiles = new ArrayList<>();
        try {
            Unmarshaller difUnmarshaller = difJaxbContext.createUnmarshaller();

            for (Dataset dataset : datasets) {

                try {
                    Resource resource = new FileSystemResource(new File(dataset.getFilenameDif()));

                    DIF dif = (DIF) difUnmarshaller.unmarshal(resource.getInputStream());
                    difFiles.add(dif);
                } catch (JAXBException ex) {
                    LOG.error("Unable to unmarshal metadata file: " + new File(dataset.getFilenameDif()));
                } catch (IOException ex) {
                    LOG.error("Unable to find file: " + new File(dataset.getFilenameDif()));
                }

            }
        } catch (JAXBException ex) {
            java.util.logging.Logger.getLogger(DIFMetadataServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return difFiles;
    }

    @Override
    public DIF getDifRecord(String identifier) {
        DIF dif = null;
        try {
            Unmarshaller difUnmarshaller = difJaxbContext.createUnmarshaller();
            if (datasetDao.notExists(identifier)) {
                dif = null;
            } else {
                Dataset dataset = datasetDao.findByIdentifier(identifier);
                Resource resource = new FileSystemResource(new File(dataset.getFilenameDif()));
                dif = (DIF) difUnmarshaller.unmarshal(resource.getInputStream());
                
                DataSetCitation landingPageCitation = new DataSetCitation();
                landingPageCitation.setOnlineResource(configuration.getString("landing.page.baseurl")+dataset.getHash());
                
                
            }
        } catch (IOException ex) {
            LOG.error("Can not read file", ex);
        } catch (JAXBException ex) {
            LOG.error("Can not unmarshall file", ex);
        }
        return dif;
    }

}
