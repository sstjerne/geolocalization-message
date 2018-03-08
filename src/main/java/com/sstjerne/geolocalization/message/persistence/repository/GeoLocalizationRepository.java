package com.sstjerne.geolocalization.message.persistence.repository;

public interface GeoLocalizationRepository {
	/**
	 * Service that is charge of get the country based on ip.
	 * 
	 * @param remoteAddr
	 * @return
	 */
	String getCountryByIP(String ip);
}
