package fr.eql.akatz.demo.petpal.spring.front.controller.webclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class GlossaryWebClient {

    private final WebClient webClient;

    public GlossaryWebClient() {
        this.webClient = WebClient.builder()
                .filter(ErrorHandler.handle())
                .baseUrl("http://localhost:8080/glossary/")
                .build();
    }

    public List<String> fetchGlossary() {
        return webClient.get()
                .uri("expressions")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }

    public String fetchExtract(String expression) {
        return webClient.get()
                .uri("extract/" + expression)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
