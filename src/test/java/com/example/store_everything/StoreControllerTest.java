package com.example.store_everything;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StoreControllerTest {

    private static final int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void test() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:8080/shared_elements/4aa6a7bb-65a3-404a-a354-5eb22540d84c",
                String.class)).contains("second");
    }




}
