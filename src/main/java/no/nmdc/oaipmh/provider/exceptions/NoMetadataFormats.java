package no.nmdc.oaipmh.provider.exceptions;

import no.nmdc.oaipmh.provider.domain.OAIPMHerrorcodeType;

/**
 * Thrown if there are no metadataformats available for the specified item.
 * 
 * Thrown by:
 * <pre>
 * ListMetadataFormats
 * </pre>
 * @author sjurl
 */
public class NoMetadataFormats extends OAIPMHException {

    public NoMetadataFormats(String message) {
        super(message, OAIPMHerrorcodeType.NO_METADATA_FORMATS);
    }
}
