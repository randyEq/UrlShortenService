package com.demo.shortenUrl.controller;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.shortenUrl.model.UrlEntity;
import com.demo.shortenUrl.service.UrlService;

import io.micrometer.core.instrument.util.StringUtils;

@RestController
@RequestMapping("api")
public class UrlController {

	@Value("${BASEURL}")
	private String BASEURL;
	
    Logger logger = LoggerFactory.getLogger(UrlController.class);
    
    @Autowired
    private UrlService urlService;

    @PostMapping(value="/encode", produces="application/json")
    public ResponseEntity<UrlEntity> getShortUrl(@RequestParam String fullUrl) {
    	
    	try {
	        // check valid full Url
	        UrlValidator validator = new UrlValidator(new String[]{"http", "https"});
	        if (fullUrl.isEmpty() || !validator.isValid(fullUrl)) {
	            logger.error("Malformed Url provided");
	            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	        }
	
	        // Get the Shortened url
	        String shortUrl = urlService.getShortUrl(fullUrl);
	        shortUrl = BASEURL + shortUrl;
	
	        logger.info(String.format("ShortUrl for FullUrl %s is %s", fullUrl, shortUrl));
	        UrlEntity urlEntity = new UrlEntity();
	        urlEntity.setShortUrl(shortUrl);
	        return new ResponseEntity<>(urlEntity, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception getting full URL: " + e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @GetMapping(value="/decode", produces="application/json")
      public ResponseEntity<UrlEntity> getFullUrl(@RequestParam String shortUrl) {
    	String shortUrlText=null;
        try {
        	// check valid short url
        	if (shortUrl.isEmpty() || !shortUrl.substring(0, 13).equals(BASEURL)) {
        		logger.error("Malformed short Url provided");
        		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        	} else {
        		shortUrlText = shortUrl.substring(13);
        	}
        	logger.info("getFullUrl():shortUrlText: " + shortUrlText);
        	
            String fullUrl = urlService.getFullUrl(shortUrlText);

            logger.info(String.format("getFullUrl(): Full Url %s", fullUrl));
	        UrlEntity urlEntity = new UrlEntity();
	        urlEntity.setFullUrl(fullUrl);
            return new ResponseEntity<>(urlEntity, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception getting full URL: " + e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

}
