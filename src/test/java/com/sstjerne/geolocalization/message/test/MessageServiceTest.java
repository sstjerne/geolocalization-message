package com.sstjerne.geolocalization.message.test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sstjerne.geolocalization.message.model.MessageModel;
import com.sstjerne.geolocalization.message.persistence.repository.GeoLocalizationRepository;
import com.sstjerne.geolocalization.message.persistence.repository.MessageRepository;
import com.sstjerne.geolocalization.message.service.impl.MessageService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService = new MessageService();
	
	@MockBean
	private MessageRepository messageRepository;

	@MockBean
	private GeoLocalizationRepository geoLocalizationRepository;
	
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void getMessage() throws Exception {
		given(this.messageRepository.save("ar","Hola from ar!")).willReturn(new MessageModel("Hola from ar!", any(Long.class) ));
		given(this.geoLocalizationRepository.getCountryByIP("127.168.55.36")).willReturn("ar");

		MessageModel messageModel = messageService.build("Hola", "127.168.55.36");
		
		Assert.assertNotNull(messageModel);
        Assert.assertEquals("Hola from ar!", messageModel.getMessage());

		

	}
}
