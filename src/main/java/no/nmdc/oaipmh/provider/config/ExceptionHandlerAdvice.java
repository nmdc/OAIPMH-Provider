/*
 */
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
import no.nmdc.oaipmh.provider.domain.VerbType;
import no.nmdc.oaipmh.provider.exceptions.BadMetadataFormatException;
import no.nmdc.oaipmh.provider.exceptions.IdDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author sjurl
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public OAIPMHtype handleException(final Exception ex) {
        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = of.createOAIPMHtype();
        System.out.println(ex.getMessage() + ex.getClass().getName());
        return oaipmh;
    }

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

    @ExceptionHandler
    @ResponseBody
    public OAIPMHtype handlecannotDisseminateFormat(final BadMetadataFormatException ex) throws DatatypeConfigurationException {
        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = of.createOAIPMHtype();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        XMLGregorianCalendar cal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        oaipmh.setResponseDate(cal2);
        OAIPMHerrorType error = of.createOAIPMHerrorType();
        error.setCode(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT);
        error.setValue(ex.getMessage());
        oaipmh.getError().add(error);
        return oaipmh;
    }

    @ExceptionHandler
    @ResponseBody
    public OAIPMHtype idDoesNotExist(final IdDoesNotExistException ex) throws DatatypeConfigurationException {
        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = of.createOAIPMHtype();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        XMLGregorianCalendar cal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        oaipmh.setResponseDate(cal2);
        OAIPMHerrorType error = of.createOAIPMHerrorType();
        error.setCode(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST);
        error.setValue(ex.getMessage());
        oaipmh.getError().add(error);
        return oaipmh;
    }
}
