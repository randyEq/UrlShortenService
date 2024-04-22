package com.demo.shortenUrl;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShortenUrlServiceApplicationTests {

    private MockMvc mockmvc;
    
    @Autowired
    private WebApplicationContext context;
    
    @Test
    public void givenFullUrlReturnStatusOk() throws Exception {
        String fullUrl = "https://example.com/foo";
        
    	mockmvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockmvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fullUrl", fullUrl))
                .andExpect(status().isOk());
    }	
    
    @Test
    public void givenFullUrlReturnJsonWithShortUrlProp() throws Exception {
        String fullUrl = "https://example.com/foo";
        mockmvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockmvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fullUrl", fullUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").exists());
    }
    
    @Test
    public void givenFullUrlReturnJsonWithShortUrlValueHasHttp() throws Exception {
        String fullUrl = "https://example.com/foo";
        mockmvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockmvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fullUrl", fullUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http")));
    }
    
    @Test
    public void shouldNotInsertFullUrlIfAlreadyExists() throws Exception {
        String fullUrl = "https://example.com/foo";
        mockmvc = MockMvcBuilders.webAppContextSetup(context).build();
        String shortUrl1 = mockmvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fullUrl", fullUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        String shortUrl2 = mockmvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fullUrl", fullUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(shortUrl1, shortUrl2);
    }
    
    @Test
    public void shouldNotInsertFullUrlIfDoesNotExist() throws Exception {
        String fullUrl1 = "https://example.com/foo1";
        String fullUrl2 = "https://example.com/foo2";
        mockmvc = MockMvcBuilders.webAppContextSetup(context).build();
        
        String shortUrl1 = mockmvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fullUrl", fullUrl1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        String shortUrl2 = mockmvc.perform(post("/api/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fullUrl", fullUrl2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        Assert.assertNotEquals(shortUrl1, shortUrl2);
    }
    
    @Test
    public void givenShortUrlNotFoundInDatabase() throws Exception {
        String shortUrl = "http://short/x";
        
    	mockmvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockmvc.perform(get("/api/decode")
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortUrl", shortUrl))
                .andExpect(status().is5xxServerError());
    }	
}
