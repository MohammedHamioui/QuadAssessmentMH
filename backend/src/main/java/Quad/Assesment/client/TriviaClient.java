package Quad.Assesment.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import Quad.Assesment.model.TriviaResponse;

@Component 
public class TriviaClient {

    private final RestTemplate restTemplate;

    //RestTemplate wordt geïnjecteerd zodat we hem kunnen mocken in tests (DI)
    public TriviaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //fetch de vragen en zet ze om naar objecten
    public TriviaResponse fetchQuestions() {
        return restTemplate.getForObject("https://opentdb.com/api.php?amount=10",TriviaResponse.class);
    }
}