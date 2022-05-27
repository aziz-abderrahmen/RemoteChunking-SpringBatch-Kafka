package com.cfa.letterjobworker;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class LetterWriter implements  ItemWriter<Letter>{


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }

    @Override
    public void write(List<? extends Letter> list) throws Exception {
        for (Letter l : list){
            final String uri = "http://localhost:9623/letter/newLetter";

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("message", l.getMessage());

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.postForObject(uri,l.getMessage(),String.class);
            System.out.println(result);
        }
    }
}
