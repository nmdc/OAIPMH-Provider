package no.nmdc.oaipmh.provider.service;

import java.util.Date;
import no.nmdc.oaipmh.provider.domain.dif.DIF;

/**
 * Service that checks if a DIF record is supposed to be included in the oai pmh
 * response based on the from and until dates provided
 *
 * @author sjurl
 */
public interface DateCheckerService {

    /**
     * Returns true if difrecords last revision date is within from and until,
     * or if revision date isn't available it checks the creation date
     *
     * @param difRecord dif record to be checked
     * @param from date that the record must have a revision date that is later
     * (can be null if until is provided)
     * @param until date that the record must have a revision date that is
     * before (can be null if from is provided)
     * @return true if record is after from and before until, if from or until
     * is null it will only check one of the dates
     */
    boolean checkDIFdates(DIF difRecord, Date from, Date until);
}
