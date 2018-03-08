package com.sstjerne.geolocalization.message.persistence.repository.impl;

import java.util.Date;
import java.util.List;

import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sstjerne.geolocalization.message.model.MessageModel;
import com.sstjerne.geolocalization.message.persistence.client.ESWrapperClientI;
import com.sstjerne.geolocalization.message.persistence.repository.MessageRepository;

@Repository
public class ESMessageRepository implements MessageRepository {

	private static Logger LOGGER = LoggerFactory.getLogger(ESMessageRepository.class.getName());

	@Autowired
	private ESWrapperClientI<Client> esWrapperClient;

	public MessageModel save(String country, String message) {
		LOGGER.debug("Entering save method country={}, message={}", country, message);
		Long timestamp = new Date().getTime();
		esWrapperClient.save(country, message, timestamp);
		return new MessageModel(message, timestamp);
	}

	@Override
	public List<MessageModel> get(int numOf) {
		return esWrapperClient.get(numOf);
	}

	@Override
	public List<MessageModel> get(int numOf, String lang) {
		return esWrapperClient.get(numOf, lang);
	}

}
