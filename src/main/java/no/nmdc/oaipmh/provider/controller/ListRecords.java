package no.nmdc.oaipmh.provider.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import no.nmdc.oaipmh.provider.domain.HeaderType;
import no.nmdc.oaipmh.provider.domain.ListRecordsType;
import no.nmdc.oaipmh.provider.domain.MetadataType;
import no.nmdc.oaipmh.provider.domain.OAIPMHtype;
import no.nmdc.oaipmh.provider.domain.ObjectFactory;
import no.nmdc.oaipmh.provider.domain.RecordType;
import no.nmdc.oaipmh.provider.domain.VerbType;
import no.nmdc.oaipmh.provider.domain.dif.DIF;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author sjurl
 */
@Controller
public class ListRecords extends HeaderGenerator {

    @Autowired()
    @Qualifier("providerConf")
    private PropertiesConfiguration configuration;

    @RequestMapping(value = "oaipmh", params = "verb=ListRecords")
    public void listRecords(@RequestParam(value = "metadataPrefix", required = false) String metadatPrefix,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from, @RequestParam(value = "until", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date until,
            @RequestParam(value = "set", required = false) String set, @RequestParam(value = "resumptionToken", required = false) String resumptionToken,
            HttpServletRequest request, HttpServletResponse response) throws DatatypeConfigurationException, IOException, JAXBException {

        ObjectFactory of = new ObjectFactory();
        OAIPMHtype oaipmh = generateOAIPMHType(request, of, VerbType.LIST_RECORDS);

        Resource metadatadir = new FileSystemResource(configuration.getString("metadata.folder"));
        File dir = metadatadir.getFile();

        File[] files = dir.listFiles();
        String metadataString = "";
        for (File file : files) {
            metadataString = metadataString.concat(generateDIFMetadata(of, file.getAbsolutePath()));
        }

        ListRecordsType lrt = of.createListRecordsType();

        oaipmh.setListRecords(lrt);

        JAXBContext oaicontext = JAXBContext.newInstance(OAIPMHtype.class);
        Marshaller oaimarshaller = oaicontext.createMarshaller();
        StringWriter oaistringwriter = new StringWriter();
        oaimarshaller.marshal(oaipmh, oaistringwriter);

        String oaiString = oaistringwriter.toString();

        String startOai = oaiString.substring(0, oaiString.indexOf("<ListRecords/>") + 14);
        startOai = startOai.replace("<ListRecords/>", "<ListRecords>");
        String endOai = oaiString.substring(oaiString.indexOf("<ListRecords/>") + 14, oaiString.length());
        endOai = "</ListRecords>".concat(endOai);

        String finalString = startOai.concat(metadataString).concat(endOai);

        IOUtils.write(finalString, response.getOutputStream());
        response.flushBuffer();

    }

    private String generateDIFMetadata(ObjectFactory of, String file) throws JAXBException, IOException {

        Resource resource = new FileSystemResource(file);
        JAXBContext difcontext = JAXBContext.newInstance(DIF.class);
        Unmarshaller unmarshaller = difcontext.createUnmarshaller();
        DIF dif = (DIF) unmarshaller.unmarshal(resource.getInputStream());

        RecordType record = of.createRecordType();
        HeaderType ht = of.createHeaderType();
        ht.setIdentifier(dif.getEntryID());
        ht.setDatestamp(dif.getLastDIFRevisionDate());
        MetadataType mt = of.createMetadataType();
        record.setMetadata(mt);
        record.setHeader(ht);

        JAXBContext oaicontext = JAXBContext.newInstance(RecordType.class);
        Marshaller oaimarshaller = oaicontext.createMarshaller();
        StringWriter recordwriter = new StringWriter();
        oaimarshaller.marshal(record, recordwriter);

        String recordString = recordwriter.toString();

        recordString = recordString.substring(recordString.indexOf("?>") + 2);
        String removeString = recordString.substring(recordString.indexOf(" "), recordString.indexOf(">", recordString.indexOf(" ")));

        recordString = recordString.replace(removeString, "");

        Marshaller ms = difcontext.createMarshaller();
        StringWriter difStringwriter = new StringWriter();
        ms.marshal(dif, difStringwriter);
        String difString = difStringwriter.toString();

        difString = difString.substring(difString.indexOf("?>") + 2);

        String startOai = recordString.substring(0, recordString.indexOf("<metadata/>") + 11);
        startOai = startOai.replace("<metadata/>", "<metadata>");
        String endOai = recordString.substring(recordString.indexOf("<metadata/>") + 11, recordString.length());
        endOai = "</metadata>".concat(endOai);

        String finalString = startOai.concat(difString).concat(endOai);

        return finalString;
    }
}
