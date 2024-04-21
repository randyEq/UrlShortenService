package com.demo.shortenUrl.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "url")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlEntity {

	@Id
	@GeneratedValue
	@JsonInclude(Include.NON_NULL)
    private Long id;
	@JsonInclude(Include.NON_NULL)
    private String fullUrl;
	@JsonInclude(Include.NON_NULL)
    private String shortUrl;

}
