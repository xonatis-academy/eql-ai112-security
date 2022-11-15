package fr.eql.akatz.demo.petpal.spring.front.controller.webclient;

import fr.eql.akatz.demo.petpal.spring.front.entity.Cat;
import fr.eql.akatz.demo.petpal.spring.front.entity.Owner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class GalleryWebClient {

    private final WebClient webClient;

    public GalleryWebClient() {
        this.webClient = WebClient.builder()
                .filter(ErrorHandler.handle())
                .baseUrl("http://localhost:8080/gallery/")
                .build();
    }

    public List<Owner> findAllOwnersButSelf(Owner self) {
       return webClient.get()
                .uri("owners/exclude/self/" + self.getId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Owner>>() {})
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
