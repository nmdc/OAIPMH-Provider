package no.nmdc.oaipmh.provider.service;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBException;
import no.nmdc.oaipmh.provider.domain.dif.DIF;

/**
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
}
