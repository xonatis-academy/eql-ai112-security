package fr.eql.akatz.demo.petpal.spring.front.controller.webclient;

import fr.eql.akatz.demo.petpal.spring.front.entity.Owner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LoginWebClient {

    private final WebClient webClient;

    public LoginWebClient() {
        this.webClient = WebClient.builder()
                .filter(ErrorHandler.handle())
                .baseUrl("http://localhost:8080/login/")
                .build();
    }

    public Owner authenticate(String login, String password) {
        return webClient.get()
                .uri(login + "/" + password)
                .retrieve()
                .bodyToMono(Owner.class)
                .block();
    }
}
