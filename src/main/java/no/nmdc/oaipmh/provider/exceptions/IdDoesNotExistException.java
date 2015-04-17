package no.nmdc.oaipmh.provider.exceptions;

import no.nmdc.oaipmh.provider.domain.OAIPMHerrorcodeType;

/**
 * Exception thrown if value of the identifier is unknown or illegal
 *
 * Thrown by:
 * <pre>
 * GetRecord
 * ListMetadataFormats
 * </pre>
 *
 * @author sjurl
 */
public class IdDoesNotExistException extends OAIPMHException {

    public IdDoesNotExistException(String message) {
        super(message, OAIPMHerrorcodeType.ID_DOES_NOT_EXIST);
    }
}
