package com.sstjerne.geolocalization.message.persistence.repository;

import java.util.List;

import com.sstjerne.geolocalization.message.model.MessageModel;

public interface MessageRepository {

	MessageModel save(String country, String message);

	List<MessageModel> get(int numOf);

	List<MessageModel> get(int numOf, String lang);

}