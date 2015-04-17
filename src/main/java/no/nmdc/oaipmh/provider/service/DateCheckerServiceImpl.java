package no.nmdc.oaipmh.provider.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import org.springframework.stereotype.Service;

/**
 *
 * @author sjurl
 */
@Service("datecheckerservice")
public class DateCheckerServiceImpl implements DateCheckerService {

    @Override
    public boolean checkDIFdates(DIF difRecord, Date from, Date until) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateToCheck = null;
            if (difRecord.getLastDIFRevisionDate() != null) {
                dateToCheck = sdf.parse(difRecord.getLastDIFRevisionDate());
            } else if (difRecord.getDIFCreationDate() != null) {
                dateToCheck = sdf.parse(difRecord.getDIFCreationDate());
            }

            if (dateToCheck == null) {
                return false;
            }

            if (from != null && until != null) {

                if (dateToCheck.after(from) && dateToCheck.before(until)) {
                    return true;
                } else {
                    return false;
                }
            } else if (from != null) {
                if (dateToCheck.after(from)) {
                    return true;
                } else {
                    return false;
                }
            } else if (until != null) {
                if (dateToCheck.before(until)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException ex) {
            return false;
        }
        return false;

    }

}
