package com.sstjerne.geolocalization.message.model;

public class MessageModel {

	private String message;
	private Long timestamp;

	public MessageModel(String message, Long timestamp) {
		this.message = message;
		this.timestamp = timestamp;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Long getTimestamp() {
		return timestamp;
	}

}
