package no.nmdc.oaipmh.provider.controller;

import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author sjurl
 */
@Controller
public class ListIdentifiers {

    @RequestMapping(value = "oaipmh", params = "verb=ListIdentifiers")
    public void listIdentifiers(@RequestParam(value = "metadataPrefix", required = false) String metadatPrefix,
            @RequestParam(value = "from", required = false) Date from, @RequestParam(value = "until", required = false) Date until,
            @RequestParam(value = "set", required = false) String set, @RequestParam(value = "resumptionToken", required = false) String resumptionToken) {

    }
}
