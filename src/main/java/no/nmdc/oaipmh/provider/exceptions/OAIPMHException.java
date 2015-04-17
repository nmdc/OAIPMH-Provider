package no.nmdc.oaipmh.provider.exceptions;

import no.nmdc.oaipmh.provider.domain.OAIPMHerrorcodeType;

/**
 * Generic OAIPMH Exception that must have an error code
 *
 * @author sjurl
 */
public class OAIPMHException extends Exception {

    private final OAIPMHerrorcodeType errorcodetype;

    public OAIPMHException(String message, OAIPMHerrorcodeType errorcodetype) {
        super(message);
        this.errorcodetype = errorcodetype;
    }

    public OAIPMHerrorcodeType getErrorCodeType() {
        return errorcodetype;
    }
}
