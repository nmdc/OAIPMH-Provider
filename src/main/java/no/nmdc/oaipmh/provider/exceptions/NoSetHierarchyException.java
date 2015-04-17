package no.nmdc.oaipmh.provider.exceptions;

import no.nmdc.oaipmh.provider.domain.OAIPMHerrorcodeType;

/**
 * Exception that shoud be cast if the repository does not support sets
 *
 * Verbs that must throw the exception if sets aren't supported:
 * <pre>
 * ListSets
 * ListIdentifiers
 * ListRecords
 * </pre>
 *
 * @author sjurl
 */
public class NoSetHierarchyException extends OAIPMHException {

    public NoSetHierarchyException(String message) {
        super(message, OAIPMHerrorcodeType.NO_SET_HIERARCHY);
    }
}
