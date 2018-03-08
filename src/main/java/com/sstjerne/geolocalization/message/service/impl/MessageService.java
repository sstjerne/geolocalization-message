package com.sstjerne.geolocalization.message.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sstjerne.geolocalization.message.model.MessageModel;
import com.sstjerne.geolocalization.message.persistence.repository.GeoLocalizationRepository;
import com.sstjerne.geolocalization.message.persistence.repository.MessageRepository;
import com.sstjerne.geolocalization.message.service.IMessageService;

@Service
public class MessageService implements IMessageService {

	private static Logger LOGGER = LoggerFactory.getLogger(MessageService.class.getName());

	@Autowired
	private MessageRepository repository;

	@Autowired
	private GeoLocalizationRepository geoLocalizationRepository;

	/**
	 * This method is charge of get the country of request, persistence and
	 * build message to return
	 * 
	 * @param message
	 * @param remoteAddr
	 * @return
	 */
	public MessageModel build(String message, String remoteAddr) {
		LOGGER.debug("Entering build method message={}, remoteAddr={}", message, remoteAddr);
		String country = geoLocalizationRepository.getCountryByIP(remoteAddr);
		String richMsg =  String.format("%s from %s!", message, country);
		return repository.save(country, richMsg);
	}

	public List<MessageModel> get(int numOf, String lang) {
		if (lang == null || "all".equals(lang))
			return repository.get(numOf);
		else
			return repository.get(numOf, lang);
	}

}
