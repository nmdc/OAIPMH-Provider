package no.nmdc.oaipmh.provider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author sjurl
 */
@Controller
public class ListSets {

    @RequestMapping(value = "oaipmh", params = "verb=ListSets")
    public void listRecords(@RequestParam(value = "resumptionToken", required = false) String resumptionToken) {

    }
}
