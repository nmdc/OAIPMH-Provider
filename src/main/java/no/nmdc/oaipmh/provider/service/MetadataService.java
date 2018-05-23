package no.nmdc.oaipmh.provider.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;
import no.nmdc.oaipmh.provider.dao.dto.Dataset;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.dif.DIF;

/**
 * Service that provides access to all dif records
 *
 * @author sjurl
 */
public interface MetadataService {

    /**
     * Returns a list of all DIF records on the server
     *
     * @return
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     */
    List<DIF> getDifRecords() throws IOException, JAXBException;

    /**
     * 
     * @return 
     */
    OAIPMHtype getSets(String resumptionToken);

    /**
     * 
     * @param set
     * @return 
     */
    List<DIF> getDifRecords(String set);
    

    DIF getDifRecord(String identifier);
    
    
    
   
}
