package fr.eql.akatz.demo.petpal.spring.back.controller.rest;

import fr.eql.akatz.demo.petpal.spring.back.entity.Cat;
import fr.eql.akatz.demo.petpal.spring.back.entity.dto.CatDto;
import fr.eql.akatz.demo.petpal.spring.back.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("space")
public class SpaceRestController {

    /** Injecté par le setter. */
    SpaceService spaceService;

    @GetMapping("owner/{id}/cats")
    public List<Cat> findOwnerCats(@PathVariable long id) {
        return spaceService.findOwnerCats(id);
    }

    @PostMapping("/cat")
    public Cat saveCat(@RequestBody CatDto catDto) {
        return spaceService.saveCat(catDto);
    }

    /// Setters ///

    @Autowired
    public void setSpaceService(SpaceService spaceService) {
        this.spaceService = spaceService;
    }
}
