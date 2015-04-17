package no.nmdc.oaipmh.provider.exceptions;

import no.nmdc.oaipmh.provider.domain.OAIPMHerrorcodeType;

/**
 * Exception thrown if the metadataPrefix argument is not supported by the item
 * or by the repository
 *
 * Thrown by
 * <pre>
 * GetRecord
 * ListIdentifiers
 * ListRecords
 * </pre>
 *
 * @author sjurl
 */
public class CannotDisseminateFormatException extends OAIPMHException {

    public CannotDisseminateFormatException(String message) {
        super(message, OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT);
    }
}
