package no.nmdc.oaipmh.provider.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import no.nmdc.oaipmh.provider.domain.ListMetadataFormatsType;
import no.nmdc.oaipmh.provider.domain.MetadataFormatType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.VerbType;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import no.nmdc.oaipmh.provider.exceptions.IdDoesNotExistException;
import no.nmdc.oaipmh.provider.exceptions.NoMetadataFormats;
import no.nmdc.oaipmh.provider.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller that handles the ListMetadataFormats verb of the oai pmh
 * specification
 *
 * @author sjurl
 */
@Controller
public class ListMetadataFormatsController extends HeaderGenerator {

    @Autowired
    private MetadataService metadataService;

    @RequestMapping(value = "oaipmh", params = "verb=ListMetadataFormats")
    public @ResponseBody
    OAIPMHtype listMetadataFormats(@RequestParam(value = "identifier", required = false) String identifier, HttpServletRequest request) throws DatatypeConfigurationException, IOException, JAXBException, IdDoesNotExistException, NoMetadataFormats {
        ObjectFactory of = new ObjectFactory();

        List<MetadataFormatType> mfts = getFormatsForId(identifier, of);

        if (mfts.isEmpty() && identifier != null) {
            throw new NoMetadataFormats("There are no metadata formats available for identifier: " + identifier);
        }

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
            boolean isfound = false;
            for (DIF dif : metadataService.getDifRecords()) {
                if (dif.getEntryID().equals(identifier)) {
                    isfound = true;
                    MetadataFormatType mft = of.createMetadataFormatType();
                    mft.setMetadataPrefix("dif");
                    mft.setMetadataNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/");
                    mft.setSchema("http://gcmd.nasa.gov/Aboutus/xml/dif/dif.xsd");
                    mfts.add(mft);
                }
            }
            if (!isfound) {
                throw new IdDoesNotExistException("Record with identifier: " + identifier + " does not exist.");
            }
            return mfts;
        }
    }
}
