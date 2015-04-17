package no.nmdc.oaipmh.provider.controller;

import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.RequestType;
import no.nmdc.oaipmh.provider.domain.VerbType;

/**
 * Class that generates the header for all calls to the OAIPMH provider. this
 * class is used by all the controllers
 *
 * @author sjurl
 */
public abstract class HeaderGenerator {

    protected OAIPMHtype generateOAIPMHType(HttpServletRequest request, ObjectFactory of, VerbType requestType) throws DatatypeConfigurationException {

        String baseUrl = String.format("%s://%s:%d/request/oaipmh", request.getScheme(), request.getServerName(), request.getServerPort());
        OAIPMHtype oaipmh = of.createOAIPMHtype();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        XMLGregorianCalendar cal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        oaipmh.setResponseDate(cal2);

        RequestType rt = generateRequestType(request, of, baseUrl, requestType);

        oaipmh.setRequest(rt);
        return oaipmh;
    }

    private RequestType generateRequestType(HttpServletRequest request, ObjectFactory of, String baseUrl, VerbType requestType) {
        RequestType rt = of.createRequestType();
        rt.setVerb(requestType);
        rt.setValue(baseUrl);

        Enumeration<String> requestParamters = request.getParameterNames();
        while (requestParamters.hasMoreElements()) {
            String requestParam = requestParamters.nextElement();
            switch (requestParam) {
                case "metadataPrefix":
                    rt.setMetadataPrefix(request.getParameter("metadataPrefix"));
                    break;
                case "from":
                    rt.setFrom(request.getParameter("from"));
                    break;
                case "until":
                    rt.setUntil(request.getParameter("until"));
                    break;
                case "set":
                    rt.setSet(request.getParameter("set"));
                    ;
                    break;
                case "resumptionToken":
                    rt.setResumptionToken(request.getParameter("resumptionToken"));
                    break;
                case "identifier":
                    rt.setIdentifier(request.getParameter("identifier"));
                    break;
            }
        }
        return rt;
    }
}
