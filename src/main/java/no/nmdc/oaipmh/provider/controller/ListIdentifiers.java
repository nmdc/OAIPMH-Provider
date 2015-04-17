package no.nmdc.oaipmh.provider.controller;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.VerbType;
import no.nmdc.oaipmh.provider.exceptions.BadMetadataFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author sjurl
 */
@Controller
public class ListIdentifiers extends HeaderGenerator {

    @RequestMapping(value = "oaipmh", params = "verb=ListIdentifiers")
    public @ResponseBody
    OAIPMHtype listIdentifiers(@RequestParam(value = "metadataPrefix", required = false) String metadatPrefix,
            @RequestParam(value = "from", required = false) Date from, @RequestParam(value = "until", required = false) Date until,
            @RequestParam(value = "set", required = false) String set, @RequestParam(value = "resumptionToken", required = false) String resumptionToken,
            HttpServletRequest request) throws DatatypeConfigurationException, BadMetadataFormatException {
        if (!metadatPrefix.equals("dif")) {
            throw new BadMetadataFormatException("metadataprefix: " + metadatPrefix + " is not known by the server.");
        }

        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = generateOAIPMHType(request, of, VerbType.LIST_IDENTIFIERS);

        return oaipmh;
    }
}
