package no.nmdc.oaipmh.provider.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import no.nmdc.oaipmh.provider.domain.HeaderType;
import no.nmdc.oaipmh.provider.domain.ListRecordsType;
import no.nmdc.oaipmh.provider.domain.MetadataType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.RecordType;
import no.nmdc.oaipmh.provider.domain.VerbType;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import no.nmdc.oaipmh.provider.exceptions.CannotDisseminateFormatException;
import no.nmdc.oaipmh.provider.exceptions.NoRecordsMatchException;
import no.nmdc.oaipmh.provider.exceptions.NoSetHierarchyException;
import no.nmdc.oaipmh.provider.service.DateCheckerService;
import no.nmdc.oaipmh.provider.service.MetadataService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.LoggerFactory;

/**
 * Controller that handles the ListRecords verb of the oai pmh specification
 *
 * As jaxb writes all namespace information on the root node we have to manually
 * return data from this class as per specification the namespace information
 * for metadata must be provided on the actual metadata
 *
 * @author sjurl
 */
@Controller
public class ListRecordsController extends HeaderGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ListRecordsController.class);

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private DateCheckerService dateCheckerService;

    @Autowired
    JAXBContext difJaxbContext;

    @Autowired
     JAXBContext recordTypeJaxbContext;
    
    @Autowired
    JAXBContext oaiJaxbContext;
    
    @RequestMapping(value = "oaipmh", params = "verb=ListRecords")
    public void listRecords(@RequestParam(value = "metadataPrefix", required = false) String metadatPrefix,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from, @RequestParam(value = "until", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date until,
            @RequestParam(value = "set", required = false) String set, @RequestParam(value = "resumptionToken", required = false) String resumptionToken,
            HttpServletRequest request, HttpServletResponse response) throws DatatypeConfigurationException, IOException, JAXBException, CannotDisseminateFormatException, NoSetHierarchyException, NoRecordsMatchException, MissingServletRequestParameterException {

        if (metadatPrefix == null && resumptionToken == null) {
            throw new MissingServletRequestParameterException("metadataPrefix", "String");
        }

        if (resumptionToken == null && !metadatPrefix.equals("dif")) {
            throw new CannotDisseminateFormatException("metadataprefix: " + metadatPrefix + " is not known by the server.");
        }

        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = generateOAIPMHType(request, of, VerbType.LIST_RECORDS);
        String metadataString = "";
        List<DIF> difs = null;
        if (set != null) {
            difs = metadataService.getDifRecords(set);
        } else {
            difs = metadataService.getDifRecords();
        }

        LOG.debug("Retrievd all diff");

        for (DIF difRecord : difs) {

            boolean include = true;
            if (from != null || until != null) {
                include = dateCheckerService.checkDIFdates(difRecord, from, until);
            }
            if (include) {
                metadataString = metadataString.concat(generateDIFMetadata(of, difRecord));
            }
        }

        if (metadataString.isEmpty()) {
            throw new NoRecordsMatchException("No records matches the query");
        }

        ListRecordsType lrt = of.createListRecordsType();

        oaipmh.setListRecords(lrt);
     
        StringWriter oaistringwriter = new StringWriter();
        oaiJaxbContext.createMarshaller().marshal(oaipmh, oaistringwriter);

        String oaiString = oaistringwriter.toString();

        String startOai = oaiString.substring(0, oaiString.indexOf("<ListRecords/>") + 14);
        startOai = startOai.replace("<ListRecords/>", "<ListRecords>");
        String endOai = oaiString.substring(oaiString.indexOf("<ListRecords/>") + 14, oaiString.length());
        endOai = "</ListRecords>".concat(endOai);

        String finalString = startOai.concat(metadataString).concat(endOai);
        LOG.debug("Final string");
        IOUtils.write(finalString, response.getOutputStream());
        response.flushBuffer();

    }

    private String generateDIFMetadata(ObjectFactory of, DIF dif) throws JAXBException, IOException {

        RecordType record = of.createRecordType();
        HeaderType ht = of.createHeaderType();
        ht.setIdentifier(dif.getEntryID());
        ht.setDatestamp(dif.getLastDIFRevisionDate());
        MetadataType mt = of.createMetadataType();
        record.setMetadata(mt);
        record.setHeader(ht);

        StringWriter recordwriter = new StringWriter();
        recordTypeJaxbContext.createMarshaller().marshal(record, recordwriter);

        String recordString = recordwriter.toString();

        recordString = recordString.substring(recordString.indexOf("?>") + 2);
        String removeString = recordString.substring(recordString.indexOf(" "), recordString.indexOf(">", recordString.indexOf(" ")));

        recordString = recordString.replace(removeString, "");

        StringWriter difStringwriter = new StringWriter();
        difJaxbContext.createMarshaller().marshal(dif, difStringwriter);
        String difString = difStringwriter.toString();

        difString = difString.substring(difString.indexOf("?>") + 2);

        String startOai = recordString.substring(0, recordString.indexOf("<metadata/>") + 11);
        startOai = startOai.replace("<metadata/>", "<metadata>");
        String endOai = recordString.substring(recordString.indexOf("<metadata/>") + 11, recordString.length());
        endOai = "</metadata>".concat(endOai);

        String finalString = startOai.concat(difString).concat(endOai);

        return finalString;
    }

}
