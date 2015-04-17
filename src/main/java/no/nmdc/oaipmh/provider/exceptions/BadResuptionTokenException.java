package no.nmdc.oaipmh.provider.exceptions;

import no.nmdc.oaipmh.provider.domain.OAIPMHerrorcodeType;

/**
 * Exception thrown if the provided resumption token is invalid or expired
 *
 * Thrown by:
 * <pre>
 * ListIdentifiers
 * ListRecords
 * ListSets
 * </pre>
 *
 * @author sjurl
 */
public class BadResuptionTokenException extends OAIPMHException {

    public BadResuptionTokenException(String message, OAIPMHerrorcodeType errorcodetype) {
        super(message, OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN);
    }

}
