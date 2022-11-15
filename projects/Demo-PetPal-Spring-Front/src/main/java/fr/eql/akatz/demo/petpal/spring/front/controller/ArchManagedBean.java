package fr.eql.akatz.demo.petpal.spring.front.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@Component(value = "mbArch")
@Scope(value = "request")
public class ArchManagedBean {

    public String navLinkStyle(String linkPageName) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String[] uriParts = request.getRequestURI().split("/");
        String currentPageName = uriParts[uriParts.length - 1];
        if (linkPageName.equals("index.xhtml") && currentPageName.equals("Demo-PetPal-Web")) {
            currentPageName = "index.xhtml";
        }
        return linkPageName.equals(currentPageName) ? "active-navlink" : "";
    }
}
