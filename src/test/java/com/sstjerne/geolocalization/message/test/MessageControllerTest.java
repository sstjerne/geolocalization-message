package com.sstjerne.geolocalization.message.test;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sstjerne.geolocalization.message.controller.MessageController;
import com.sstjerne.geolocalization.message.model.MessageModel;
import com.sstjerne.geolocalization.message.service.impl.MessageService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MessageService messageService;

	@Test
	public void getMessage() throws Exception {
		List<MessageModel> value = new ArrayList<>();
		long time = new Date().getTime();
		value.add(new MessageModel("Hola from ar!", time));
		given(this.messageService.get(10, "ar")).willReturn(value);

		this.mvc.perform(MockMvcRequestBuilders.get("?numOf=10&lang=ar").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("[{\"message\":\"Hola from ar!\",\"timestamp\":" + time + "}]"));

	}
}
