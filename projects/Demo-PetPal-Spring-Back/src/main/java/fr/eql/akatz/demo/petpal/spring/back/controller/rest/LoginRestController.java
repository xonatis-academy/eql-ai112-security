package fr.eql.akatz.demo.petpal.spring.back.controller.rest;

import fr.eql.akatz.demo.petpal.spring.back.entity.Owner;
import fr.eql.akatz.demo.petpal.spring.back.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginRestController {

    /** Injecté par le setter. */
    private LoginService loginService;

    @GetMapping("/{login}/{password}")
    public Owner authenticate(@PathVariable String login, @PathVariable String password) {
        return loginService.authenticate(login, password);
    }

    /// Setters ///

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }
}
