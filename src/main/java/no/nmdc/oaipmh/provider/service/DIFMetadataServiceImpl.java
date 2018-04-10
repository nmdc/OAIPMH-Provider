package no.nmdc.oaipmh.provider.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import no.nmdc.oaipmh.provider.dao.DatasetDao;
import no.nmdc.oaipmh.provider.dao.dto.Dataset;
import no.nmdc.oaipmh.provider.domain.ListSetsType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import org.apache.commons.configuration.PropertiesConfiguration;
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
    private DatasetDao datasetDao;

    @Override
    public List<DIF> getDifRecords() throws IOException {
        Resource metadatadir = new FileSystemResource(configuration.getString("metadata.folder"));
        File dir = metadatadir.getFile();

        File[] files = dir.listFiles();
        List<DIF> difFiles = new ArrayList<>();
        try {
            JAXBContext difcontext = JAXBContext.newInstance(DIF.class);

            for (File file : files) {
                try {
                    Resource resource = new FileSystemResource(file);

                    Unmarshaller unmarshaller = difcontext.createUnmarshaller();

                    DIF dif = (DIF) unmarshaller.unmarshal(resource.getInputStream());
                    difFiles.add(dif);
                } catch (JAXBException ex) {
                    LoggerFactory.getLogger(DIFMetadataServiceImpl.class).error("Unable to unmarshal metadata file: " + file.getAbsolutePath());
                }

            }
        } catch (JAXBException ex) {
            LoggerFactory.getLogger(DIFMetadataServiceImpl.class).error("Unable to create JAXB Context");
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
        for (Dataset dataset : datasets) {
            try {
                Resource resource = new FileSystemResource(new File(dataset.getFilenameDif()));
                JAXBContext difcontext = JAXBContext.newInstance(DIF.class);
                Unmarshaller unmarshaller = difcontext.createUnmarshaller();

                DIF dif = (DIF) unmarshaller.unmarshal(resource.getInputStream());
                difFiles.add(dif);
            } catch (JAXBException ex) {
                LoggerFactory.getLogger(DIFMetadataServiceImpl.class).error("Unable to unmarshal metadata file: " + new File(dataset.getFilenameDif()));
            } catch (IOException ex) {
                LoggerFactory.getLogger(DIFMetadataServiceImpl.class).error("Unable to find file: " + new File(dataset.getFilenameDif()));
            }

        }
        return difFiles;
    }

}
