package no.nmdc.oaipmh.provider.config;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import no.nmdc.oaipmh.provider.domain.OAIPMHerrorType;
import no.nmdc.oaipmh.provider.domain.OAIPMHerrorcodeType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.exceptions.OAIPMHException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Class that handles exceptions and gives the user the correct feedback
 *
 * @author sjurl
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * Special handler for BAD_ARGUMENT as this is triggered by a
     * MissingServletRequestParameterException from spring
     *
     * @param ex
     * @return
     * @throws DatatypeConfigurationException
     */
    @ExceptionHandler
    @ResponseBody
    public OAIPMHtype handleMissingParameter(final MissingServletRequestParameterException ex) throws DatatypeConfigurationException {
        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = of.createOAIPMHtype();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        XMLGregorianCalendar cal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        oaipmh.setResponseDate(cal2);
        OAIPMHerrorType error = of.createOAIPMHerrorType();
        error.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
        error.setValue(ex.getMessage());
        oaipmh.getError().add(error);
        return oaipmh;
    }

    /**
     * Special handling of BAD_VERB exception as this is triggered by
     * UnsatisfiedServletRequestParameterException from spring
     *
     * @param ex
     * @return
     * @throws DatatypeConfigurationException
     */
    @ExceptionHandler
    @ResponseBody
    public OAIPMHtype handleBadVerb(final UnsatisfiedServletRequestParameterException ex) throws DatatypeConfigurationException {
        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = of.createOAIPMHtype();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        XMLGregorianCalendar cal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        oaipmh.setResponseDate(cal2);
        OAIPMHerrorType error = of.createOAIPMHerrorType();
        error.setCode(OAIPMHerrorcodeType.BAD_VERB);
        error.setValue(ex.getMessage());
        oaipmh.getError().add(error);
        return oaipmh;
    }

    /**
     * Handles all other exceptions that can be thrown by the supported verbs
     * and returns a correctly formatted exception
     *
     * @param ex
     * @return
     * @throws DatatypeConfigurationException
     */
    @ExceptionHandler
    @ResponseBody
    public OAIPMHtype noRecordsMatch(final OAIPMHException ex) throws DatatypeConfigurationException {
        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = of.createOAIPMHtype();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        XMLGregorianCalendar cal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        oaipmh.setResponseDate(cal2);
        OAIPMHerrorType error = of.createOAIPMHerrorType();
        error.setCode(ex.getErrorCodeType());
        error.setValue(ex.getMessage());
        oaipmh.getError().add(error);
        return oaipmh;
    }
}
