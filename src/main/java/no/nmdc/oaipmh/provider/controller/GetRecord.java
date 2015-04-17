package no.nmdc.oaipmh.provider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author sjurl
 */
@Controller
public class GetRecord {

    @RequestMapping(value = "oaipmh", params = "verb=GetRecord")
    public void getRecord(@RequestParam("identifier") String identifier, @RequestParam("metadataPrefix") String metadataprefix) {

    }
}
