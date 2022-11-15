package fr.eql.akatz.demo.petpal.spring.back.controller.rest;

import fr.eql.akatz.demo.petpal.spring.back.entity.Cat;
import fr.eql.akatz.demo.petpal.spring.back.entity.Owner;
import fr.eql.akatz.demo.petpal.spring.back.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("gallery")
public class GalleryRestController {

    /** Injecté par le setter. */
    GalleryService galleryService;

    @GetMapping("owner/{id}/cats")
    public List<Cat> findOwnerCats(@PathVariable long id) {
        return galleryService.findOwnerCats(id);
    }

    @GetMapping("owners/exclude/self/{id}")
    public List<Owner> findAllOwnersButSelf(@PathVariable long id) {
        return galleryService.findAllOwnersButSelf(id);
    }

    /// Setters ///

    @Autowired
    public void setGalleryService(GalleryService galleryService) {
        this.galleryService = galleryService;
    }
}
