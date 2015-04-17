package no.nmdc.oaipmh.provider.controller;

import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import no.nmdc.oaipmh.provider.domain.HeaderType;
import no.nmdc.oaipmh.provider.domain.ListIdentifiersType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.VerbType;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import no.nmdc.oaipmh.provider.exceptions.CannotDisseminateFormatException;
import no.nmdc.oaipmh.provider.exceptions.NoSetHierarchyException;
import no.nmdc.oaipmh.provider.service.DateCheckerService;
import no.nmdc.oaipmh.provider.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller that handles the ListIdentifiers verb of the oai pmh specification
 *
 * @author sjurl
 */
@Controller
public class ListIdentifiersController extends HeaderGenerator {

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private DateCheckerService dateCheckerService;

    @RequestMapping(value = "oaipmh", params = "verb=ListIdentifiers")
    public @ResponseBody
    OAIPMHtype listIdentifiers(@RequestParam(value = "metadataPrefix", required = false) String metadatPrefix,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
            @RequestParam(value = "until", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date until,
            @RequestParam(value = "set", required = false) String set, @RequestParam(value = "resumptionToken", required = false) String resumptionToken,
            HttpServletRequest request) throws DatatypeConfigurationException, CannotDisseminateFormatException, NoSetHierarchyException, MissingServletRequestParameterException, IOException, JAXBException {

        if (metadatPrefix == null && resumptionToken == null) {
            throw new MissingServletRequestParameterException("metadataPrefix", "String");
        }

        if (resumptionToken == null && !metadatPrefix.equals("dif")) {
            throw new CannotDisseminateFormatException("metadataprefix: " + metadatPrefix + " is not known by the server.");
        }

        if (set != null) {
            throw new NoSetHierarchyException("The server does not support sets");
        }

        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = generateOAIPMHType(request, of, VerbType.LIST_IDENTIFIERS);
        ListIdentifiersType lit = of.createListIdentifiersType();

        for (DIF record : metadataService.getDifRecords()) {
            boolean include = true;
            if (from != null || until != null) {
                include = dateCheckerService.checkDIFdates(record, from, until);
            }
            if (include) {
                HeaderType header = of.createHeaderType();
                header.setIdentifier(record.getEntryID());
                if (record.getLastDIFRevisionDate() != null) {
                    header.setDatestamp(record.getLastDIFRevisionDate());
                } else {
                    header.setDatestamp(record.getDIFCreationDate());
                }
                lit.getHeader().add(header);
            }
        }
        oaipmh.setListIdentifiers(lit);
        return oaipmh;
    }
}
