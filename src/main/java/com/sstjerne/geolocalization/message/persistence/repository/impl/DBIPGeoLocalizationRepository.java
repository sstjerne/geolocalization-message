package com.sstjerne.geolocalization.message.persistence.repository.impl;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstjerne.geolocalization.message.persistence.repository.GeoLocalizationRepository;

@PropertySources(value = @PropertySource({ "classpath:application.properties" }) )
@Repository
public class DBIPGeoLocalizationRepository implements GeoLocalizationRepository {

	private static Logger LOGGER = LoggerFactory.getLogger(DBIPGeoLocalizationRepository.class.getName());
	
	@Autowired
	private Environment env;
	
	private String resourceUrl;

	@PostConstruct
	public void initialize() throws Exception {

		String apikey = env.getProperty("db.ip.api.key");
		
		this.resourceUrl = "http://api.db-ip.com/v2/" + apikey;

	}
	

	public String getCountryByIP(String ip) {
		LOGGER.debug("Entering find getCountryByIP remoteAddr={}", ip);

		//http://api.db-ip.com/v2/<apiKey>/<ipAddress>

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl + "/" + ip, String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(response.getBody());
			JsonNode countryCode = root.path("countryCode");
			return countryCode.asText().toLowerCase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "n_a";
		
	}

}
