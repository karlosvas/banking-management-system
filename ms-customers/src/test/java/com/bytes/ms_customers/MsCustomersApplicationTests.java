package com.bytes.ms_customers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "JWT_SECRET_KEY=test-secret-key-for-testing-only-must-be-long-enough-for-hs256"
})
class MsCustomersApplicationTests {

	@Test
	void contextLoads() {
	}

}
