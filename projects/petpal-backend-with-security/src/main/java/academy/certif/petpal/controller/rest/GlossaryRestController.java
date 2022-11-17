package academy.certif.petpal.controller.rest;

import academy.certif.petpal.service.GlossaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /*
     * PreAuthorize supports SpEL (Spring Expression Language):
     * @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
     * Without SpEL, it's quite the same as:
     * @RolesAllowed({ "ROLE_ADMIN", "ROLE_EDITOR" }) or just: @RolesAllowed("ROLE_ADMIN")
     * 
     * PS: @RolesAllowed requires JSR-250: jsr250Enabled = true in WebSecurityConfigurerAdapter
     * PS: @Secured without JSR-250 requires: securedEnabled  = true in WebSecurityConfigurerAdapter
     */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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



