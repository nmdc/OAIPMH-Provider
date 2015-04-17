package no.nmdc.oaipmh.provider.controller;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import no.nmdc.oaipmh.provider.domain.ListMetadataFormatsType;
import no.nmdc.oaipmh.provider.domain.MetadataFormatType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.RequestType;
import no.nmdc.oaipmh.provider.domain.VerbType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author sjurl
 */
@Controller
public class ListMetadataFormats {

    @RequestMapping(value = "oaipmh", params = "verb=ListMetadataFormats")
    public @ResponseBody
    OAIPMHtype listMetadataFormats(@RequestParam(value = "identifier", required = false) String identifier, HttpServletRequest request) throws DatatypeConfigurationException {
        ObjectFactory of = new ObjectFactory();
        String baseUrl = String.format("%s://%s:%d/request/oaipmh", request.getScheme(), request.getServerName(), request.getServerPort());
        OAIPMHtype oaipmh = of.createOAIPMHtype();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        XMLGregorianCalendar cal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        oaipmh.setResponseDate(cal2);
        RequestType rt = of.createRequestType();
        rt.setVerb(VerbType.LIST_METADATA_FORMATS);
        rt.setValue(baseUrl);
        oaipmh.setRequest(rt);

        ListMetadataFormatsType listmft = of.createListMetadataFormatsType();

        MetadataFormatType mft = of.createMetadataFormatType();
        mft.setMetadataPrefix("dif");
        mft.setMetadataNamespace("http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/");
        mft.setSchema("http://gcmd.nasa.gov/Aboutus/xml/dif/dif.xsd");

        listmft.getMetadataFormat().add(mft);

        oaipmh.setListMetadataFormats(listmft);

        return oaipmh;
    }
}
