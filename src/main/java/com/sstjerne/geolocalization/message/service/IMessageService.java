package com.sstjerne.geolocalization.message.service;

import java.util.List;

import com.sstjerne.geolocalization.message.model.MessageModel;

public interface IMessageService {

	/**
	 * This method is charge of get the country of request, persistence and
	 * build message to return
	 * 
	 * @param message
	 * @param remoteAddr
	 * @return
	 */
	MessageModel build(String message, String remoteAddr);

	/**
	 * This method is charge of get the messages from store based on country
	 * 
	 * @param numOf size of response
	 * @param lang 
	 * @return
	 */
	List<MessageModel> get(int numOf, String lang);

}
