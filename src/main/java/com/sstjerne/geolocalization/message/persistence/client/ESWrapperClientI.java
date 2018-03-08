package com.sstjerne.geolocalization.message.persistence.client;

import java.util.List;

import com.sstjerne.geolocalization.message.model.MessageModel;

public interface ESWrapperClientI<T> {

	void restart() throws Exception;

	T getClient();

	void verifyConnection() throws Exception;

	/**
	 * Persistence message & timestamp in lang index
	 * 
	 * @param country
	 * @param message
	 * @param timestamp
	 * @return
	 */
	boolean save(String lang, String message, Long timestamp);

	/**
	 * Return last inputs messages indistinct from the lang. 'numOf' as max
	 * of messages to return.
	 * 
	 * @param numOf
	 * @return
	 */
	List<MessageModel> get(int numOf);

	/**
	 * Return last inputs messages by lang. 'numOf' as max
	 * of messages to return.
	 *  
	 * @param numOf
	 * @param lang
	 * @return
	 */
	List<MessageModel> get(int numOf, String lang);

}
