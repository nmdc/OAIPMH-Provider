package no.nmdc.oaipmh.provider.controller;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import no.nmdc.oaipmh.provider.domain.DeletedRecordType;
import no.nmdc.oaipmh.provider.domain.GranularityType;
import no.nmdc.oaipmh.provider.domain.IdentifyType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.RequestType;
import no.nmdc.oaipmh.provider.domain.VerbType;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author sjurl
 */
@Controller
public class Identify extends HeaderGenerator {

    @Autowired()
    @Qualifier("providerConf")
    private PropertiesConfiguration configuration;

    @RequestMapping(value = "oaipmh", params = "verb=identify")
    public @ResponseBody
    OAIPMHtype identify(HttpServletRequest request) throws DatatypeConfigurationException {
        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = generateOAIPMHType(request, of, VerbType.IDENTIFY);
        IdentifyType identify = of.createIdentifyType();
        identify.setRepositoryName(configuration.getString("identify.repository.name"));
        String baseUrl = String.format("%s://%s:%d/request/oaipmh", request.getScheme(), request.getServerName(), request.getServerPort());
        identify.setBaseURL(baseUrl);
        identify.setProtocolVersion("2.0");

        List<String> adminEmails = (List<String>) (List<?>) configuration.getList("identify.admin.email");
        for (String adminEmail : adminEmails) {
            identify.getAdminEmail().add(adminEmail);
        }

        identify.setDeletedRecord(DeletedRecordType.NO);
        identify.setGranularity(GranularityType.YYYY_MM_DD);
        oaipmh.setIdentify(identify);
        return oaipmh;
    }
}
