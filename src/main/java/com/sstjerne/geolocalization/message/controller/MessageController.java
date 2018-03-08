package com.sstjerne.geolocalization.message.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sstjerne.geolocalization.message.model.MessageModel;
import com.sstjerne.geolocalization.message.service.impl.MessageService;

@RestController
public class MessageController {

	@Autowired
	private MessageService service;

	@RequestMapping(value = "/{message}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public HttpEntity<MessageModel> index(@PathVariable("message") final String message, final HttpServletRequest request) {

		/**
		 * if user is behind a proxy server or access your web server through a
		 * load balancer (for example, in cloud hosting), the above code will
		 * get the IP address of the proxy server or load balancer server, not
		 * the original IP address of a client. To solve it, you should get the
		 * IP address of the request’s HTTP header “X-Forwarded-For (XFF)“.
		 */
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		MessageModel msg = service.build(message, ipAddress);
		return new HttpEntity<MessageModel>(msg);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public HttpEntity<List<MessageModel>> index(@RequestParam(value = "numOf", defaultValue = "10", required = false) final int numOf, 
			@RequestParam(value = "lang", required = false) final String lang, final HttpServletRequest request) {
		
		List<MessageModel> messages = service.get(numOf, lang);
		return new HttpEntity<List<MessageModel>>(messages);
	}
}
