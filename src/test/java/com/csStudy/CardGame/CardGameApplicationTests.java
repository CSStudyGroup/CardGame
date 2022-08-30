package com.csStudy.CardGame;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CardGameApplicationTests {

	@Test
	void contextLoads() {

	}

}
