
package no.nmdc.oaipmh.provider.dao;

import java.util.List;
import no.nmdc.oaipmh.provider.dao.dto.Dataset;
import no.nmdc.oaipmh.provider.domain.SetType;

/**
 *
 * @author kjetilf
 */
public interface DatasetDao {
    /**
     * 
     * @param providerurl
     * @param identifier
     * @param set
     * @param format
     * @param filenameHarvested
     * @param filenameDif
     * @param filenameNmdc
     * @param filenameHtml 
     * @param hash 
     * @param originatingCenter 
     */
    void insert(String providerurl, String identifier, String set, String format, String filenameHarvested, String filenameDif, String filenameNmdc, String filenameHtml, String hash, String originatingCenter);
    /**
     * 
     * @param baseUrl
     * @param identifier
     * @param set
     * @param metadataNamespace
     * @param filenameHarvested
     * @param filenameDif
     * @param filenameNmdc
     * @param filenameHtml
     * @param hash
     * @param originatingCenter 
     */
    void update(String baseUrl, String identifier, String set, String metadataNamespace, String filenameHarvested, String filenameDif, String filenameNmdc, String filenameHtml, String hash, String originatingCenter);
    /**
     * 
     * @param identifer
     * @return 
     */
    boolean notExists(String identifer);

    Dataset findByFilenameHarvested(String filenameHarvested);
    
    Dataset findByIdentifier(String identifier);

    List<SetType> getDistinctOriginatingCenters();

    List<Dataset> findByOriginatingCenter(String set);

    List<Dataset> findBySet(String set);

    List<SetType> listSets();
    
}
