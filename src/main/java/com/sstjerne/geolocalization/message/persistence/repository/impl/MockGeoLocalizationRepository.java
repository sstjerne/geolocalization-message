package com.sstjerne.geolocalization.message.persistence.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sstjerne.geolocalization.message.persistence.repository.GeoLocalizationRepository;

//@Repository
public class MockGeoLocalizationRepository implements GeoLocalizationRepository {

	private static Logger LOGGER = LoggerFactory.getLogger(MockGeoLocalizationRepository.class.getName());

	public String getCountryByIP(String ip) {
		LOGGER.debug("Entering find method remoteAddr={}", ip);
		return "en";
	}

}
