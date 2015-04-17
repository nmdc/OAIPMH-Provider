package no.nmdc.oaipmh.provider.controller;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.exceptions.NoSetHierarchyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller that handles the ListSets verb of the oai pmh specification
 *
 * @author sjurl
 */
@Controller
public class ListSetsController extends HeaderGenerator {

    @RequestMapping(value = "oaipmh", params = "verb=ListSets")
    public @ResponseBody
    OAIPMHtype listSets(@RequestParam(value = "resumptionToken", required = false) String resumptionToken,
            HttpServletRequest request) throws DatatypeConfigurationException, NoSetHierarchyException {
        throw new NoSetHierarchyException("The server does not support sets");
//        ObjectFactory of = new ObjectFactory();
//        OAIPMHtype oaipmh = generateOAIPMHType(request, of, VerbType.LIST_SETS);
//        return oaipmh;
    }
}
