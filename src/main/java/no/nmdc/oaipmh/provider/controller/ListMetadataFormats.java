package no.nmdc.oaipmh.provider.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import no.nmdc.oaipmh.provider.domain.ListMetadataFormatsType;
import no.nmdc.oaipmh.provider.domain.MetadataFormatType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.VerbType;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import no.nmdc.oaipmh.provider.exceptions.IdDoesNotExistException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author sjurl
 */
@Controller
public class ListMetadataFormats extends HeaderGenerator {

    @Autowired()
    @Qualifier("providerConf")
    private PropertiesConfiguration configuration;

    @RequestMapping(value = "oaipmh", params = "verb=ListMetadataFormats")
    public @ResponseBody
    OAIPMHtype listMetadataFormats(@RequestParam(value = "identifier", required = false) String identifier, HttpServletRequest request) throws DatatypeConfigurationException, IOException, JAXBException, IdDoesNotExistException {
        ObjectFactory of = new ObjectFactory();

        List<MetadataFormatType> mfts = getFormatsForId(identifier, of);

        OAIPMHtype oaipmh = generateOAIPMHType(request, of, VerbType.LIST_METADATA_FORMATS);

        ListMetadataFormatsType listmft = of.createListMetadataFormatsType();
        for (MetadataFormatType metadataFormatType : mfts) {
            listmft.getMetadataFormat().add(metadataFormatType);
        }

        oaipmh.setListMetadataFormats(listmft);

        return oaipmh;
    }

    private List<MetadataFormatType> getFormatsForId(String identifier, ObjectFactory of) throws IOException, JAXBException, IdDoesNotExistException {
        List<MetadataFormatType> mfts = new ArrayList<>();
        if (identifier == null) {
            MetadataFormatType mft = of.createMetadataFormatType();
            mft.setMetadataPrefix("dif");
            mft.setMetadataNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/");
            mft.setSchema("http://gcmd.nasa.gov/Aboutus/xml/dif/dif.xsd");
            mfts.add(mft);

            return mfts;
        } else {
            Resource metadatadir = new FileSystemResource(configuration.getString("metadata.folder"));
            File dir = metadatadir.getFile();

            File[] files = dir.listFiles();

            for (File file : files) {
                Resource resource = new FileSystemResource(file);
                JAXBContext difcontext = JAXBContext.newInstance(DIF.class);
                Unmarshaller unmarshaller = difcontext.createUnmarshaller();
                DIF dif = (DIF) unmarshaller.unmarshal(resource.getInputStream());
                if (dif.getEntryID().equals(identifier)) {
                    MetadataFormatType mft = of.createMetadataFormatType();
                    mft.setMetadataPrefix("dif");
                    mft.setMetadataNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/");
                    mft.setSchema("http://gcmd.nasa.gov/Aboutus/xml/dif/dif.xsd");
                    mfts.add(mft);
                }
            }
            if (mfts.isEmpty()) {
                throw new IdDoesNotExistException("Record with identifier: " + identifier + " does not exist.");
            }
            return mfts;
        }
    }
}
