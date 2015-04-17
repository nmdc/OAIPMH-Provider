package no.nmdc.oaipmh.provider.service;

import java.util.Date;
import no.nmdc.oaipmh.provider.domain.dif.DIF;

/**
 *
 * @author sjurl
 */
public interface DateCheckerService {

    boolean checkDIFdates(DIF difRecord, Date from, Date until);
}
