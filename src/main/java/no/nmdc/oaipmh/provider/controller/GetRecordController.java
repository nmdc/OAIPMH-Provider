package no.nmdc.oaipmh.provider.controller;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import no.nmdc.oaipmh.provider.domain.GetRecordType;
import no.nmdc.oaipmh.provider.domain.HeaderType;
import no.nmdc.oaipmh.provider.domain.MetadataType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.RecordType;
import no.nmdc.oaipmh.provider.domain.VerbType;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import no.nmdc.oaipmh.provider.exceptions.CannotDisseminateFormatException;
import no.nmdc.oaipmh.provider.exceptions.IdDoesNotExistException;
import no.nmdc.oaipmh.provider.service.MetadataService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller that handles the GetRecord verb of the oai pmh specification.
 *
 * As jaxb writes all namespace information on the root node we have to manually
 * return data from this class as per specification the namespace information
 * for metadata must be provided on the actual metadata
 *
 * @author sjurl
 */
@Controller
public class GetRecordController extends HeaderGenerator {

    @Autowired
    private MetadataService metadataService;

    @RequestMapping(value = "oaipmh", params = "verb=GetRecord")
    public void getRecord(@RequestParam("identifier") String identifier, @RequestParam("metadataPrefix") String metadataprefix,
            HttpServletRequest request, HttpServletResponse response) throws DatatypeConfigurationException, CannotDisseminateFormatException, IOException, JAXBException, IdDoesNotExistException {
        if (!metadataprefix.equals("dif")) {
            throw new CannotDisseminateFormatException("metadataprefix: " + metadataprefix + " is not known by the server.");
        }

        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = generateOAIPMHType(request, of, VerbType.LIST_RECORDS);

        String metadataString = "";
        for (DIF record : metadataService.getDifRecords()) {
            if (record.getEntryID().equals(identifier)) {
                metadataString = metadataString.concat(generateDIFMetadata(of, record));
            }
        }
        if (metadataString.isEmpty()) {
            throw new IdDoesNotExistException("Record with identifier: " + identifier + " does not exist.");
        }

        GetRecordType getRecord = of.createGetRecordType();

        oaipmh.setGetRecord(getRecord);

        JAXBContext oaicontext = JAXBContext.newInstance(OAIPMHtype.class);
        Marshaller oaimarshaller = oaicontext.createMarshaller();
        StringWriter oaistringwriter = new StringWriter();
        oaimarshaller.marshal(oaipmh, oaistringwriter);

        String oaiString = oaistringwriter.toString();

        String startOai = oaiString.substring(0, oaiString.indexOf("<GetRecord/>") + 12);
        startOai = startOai.replace("<GetRecord/>", "<GetRecord>");
        String endOai = oaiString.substring(oaiString.indexOf("<GetRecord/>") + 12, oaiString.length());
        endOai = "</GetRecord>".concat(endOai);

        String finalString = startOai.concat(metadataString).concat(endOai);

        IOUtils.write(finalString, response.getOutputStream());
        response.flushBuffer();
    }

    private String generateDIFMetadata(ObjectFactory of, DIF dif) throws JAXBException, IOException {

        JAXBContext difcontext = JAXBContext.newInstance(DIF.class);

        RecordType record = of.createRecordType();
        HeaderType ht = of.createHeaderType();
        ht.setIdentifier(dif.getEntryID());
        ht.setDatestamp(dif.getLastDIFRevisionDate());
        MetadataType mt = of.createMetadataType();
        record.setMetadata(mt);
        record.setHeader(ht);

        JAXBContext oaicontext = JAXBContext.newInstance(RecordType.class);
        Marshaller oaimarshaller = oaicontext.createMarshaller();
        StringWriter recordwriter = new StringWriter();
        oaimarshaller.marshal(record, recordwriter);

        String recordString = recordwriter.toString();

        recordString = recordString.substring(recordString.indexOf("?>") + 2);
        String removeString = recordString.substring(recordString.indexOf(" "), recordString.indexOf(">", recordString.indexOf(" ")));

        recordString = recordString.replace(removeString, "");

        Marshaller ms = difcontext.createMarshaller();
        StringWriter difStringwriter = new StringWriter();
        ms.marshal(dif, difStringwriter);
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
