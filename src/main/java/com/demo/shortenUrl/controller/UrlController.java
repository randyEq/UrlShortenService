package com.demo.shortenUrl.controller;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.shortenUrl.service.UrlService;

@RestController
@RequestMapping("api")
public class UrlController {

	public static final String BASEURL = "http://short/";
    Logger logger = LoggerFactory.getLogger(UrlController.class);
    
    @Autowired
    private UrlService urlService;

    @PostMapping("/encode")
    public ResponseEntity<String> getShortUrl(@RequestParam String fullUrl) {
    
    	String baseUrl = null;
    	
    	try {
	        // check valid Url
	        UrlValidator validator = new UrlValidator(new String[]{"http", "https"});
	        if (!validator.isValid(fullUrl)) {
	            logger.error("Malformed Url provided");
	            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	        }
	
	        // Get the Shortened url
	        String shortUrl = urlService.getShortUrl(fullUrl);
	        shortUrl = BASEURL + shortUrl;
	
	        logger.info(String.format("ShortUrl for FullUrl %s is %s", fullUrl, shortUrl));
	
	        return new ResponseEntity<>(shortUrl, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception getting full URL: " + e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @GetMapping("/decode")
      public ResponseEntity<String> getFullUrl(@RequestParam String shortUrl) {
        try {
            String fullUrl = urlService.getFullUrl(shortUrl);

            logger.info(String.format("Full Url %s", fullUrl));

            return new ResponseEntity<>(fullUrl, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception getting full URL: " + e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

}
