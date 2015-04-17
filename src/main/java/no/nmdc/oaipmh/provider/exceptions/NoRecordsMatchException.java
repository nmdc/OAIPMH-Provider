package no.nmdc.oaipmh.provider.exceptions;

import no.nmdc.oaipmh.provider.domain.OAIPMHerrorcodeType;

/**
 * Exception that should be thrown if no records can be found that matches the
 * from, until, set and metadataPrefix provided by the user.
 *
 * thrown by:
 * <pre>
 * ListIdentifiers
 * ListRecords
 * </pre>
 *
 * @author sjurl
 */
public class NoRecordsMatchException extends OAIPMHException {

    public NoRecordsMatchException(String message) {
        super(message, OAIPMHerrorcodeType.NO_RECORDS_MATCH);
    }
}
