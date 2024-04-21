package com.demo.shortenUrl.model;

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
    private Long id;
    private String fullUrl;
    private String shortUrl;

}
