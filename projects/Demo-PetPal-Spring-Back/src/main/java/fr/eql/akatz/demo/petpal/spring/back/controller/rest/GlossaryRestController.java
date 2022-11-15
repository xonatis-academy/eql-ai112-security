package fr.eql.akatz.demo.petpal.spring.back.controller.rest;

import fr.eql.akatz.demo.petpal.spring.back.service.GlossaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("glossary")
public class GlossaryRestController {

    /** Injecté par le setter. */
    GlossaryService glossaryService;

    @GetMapping("/expressions")
    public List<String> findGlossaryExpressions() {
        return glossaryService.findGlossaryExpressions();
    }

    @GetMapping("/extract/{expression}")
    public String fetchExtract(@PathVariable String expression) {
        return glossaryService.fetchExtract(expression);
    }

    /// Setters ///

    @Autowired
    public void setGlossaryService(GlossaryService glossaryService) {
        this.glossaryService = glossaryService;
    }
}
