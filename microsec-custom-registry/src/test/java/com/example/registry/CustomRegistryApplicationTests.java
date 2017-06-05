package com.example.registry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import microsec.freddysbbq.customregistry.CustomRegistryApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CustomRegistryApplication.class)
@WebAppConfiguration
public class CustomRegistryApplicationTests {

	@Test
	public void contextLoads() {
	}

}
