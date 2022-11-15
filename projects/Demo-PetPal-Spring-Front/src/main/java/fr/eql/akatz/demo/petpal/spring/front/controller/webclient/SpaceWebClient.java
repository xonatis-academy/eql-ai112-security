package fr.eql.akatz.demo.petpal.spring.front.controller.webclient;

import fr.eql.akatz.demo.petpal.spring.front.entity.Cat;
import fr.eql.akatz.demo.petpal.spring.front.entity.Owner;
import fr.eql.akatz.demo.petpal.spring.front.entity.dto.CatDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpaceWebClient {

    private final WebClient webClient;

    public SpaceWebClient() {
        this.webClient = WebClient.builder()
                .filter(ErrorHandler.handle())
                .baseUrl("http://localhost:8080/space/")
                .build();
    }

    public Cat saveCat(CatDto catDto) {
        return webClient.post()
                .uri("cat")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(catDto)
                .retrieve()
                .bodyToMono(Cat.class)
                .block();
    }

    public Owner getOwnerUpdatedWithPets(Owner owner) {
         List<Cat> cats = webClient.get()
                .uri("owner/" + owner.getId() + "/cats")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Cat>>() {})
                .block();
         owner.setPets(new ArrayList<>());
         if (cats != null) {
             owner.getPets().addAll(cats);
         }
        return owner;
    }
}
