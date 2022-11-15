package fr.eql.akatz.demo.petpal.spring.front.controller;

import fr.eql.akatz.demo.petpal.spring.front.controller.webclient.GlossaryWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component(value = "mbGlossary")
@Scope(value = "request")
public class GlossaryManagedBean {

    /** Injecté par le setter. */
    private GlossaryWebClient glossaryWebClient;

    private List<String> expressions;
    private String selectedExpression = "";

    @PostConstruct
    public void init() {
        expressions = glossaryWebClient.fetchGlossary();
        if (!expressions.isEmpty()) {
            selectedExpression = expressions.get(0);
        }
    }

    public String fetchExtract() {
        return glossaryWebClient.fetchExtract(selectedExpression);
    }

    public void updateSelectedExpression(String expression) {
        selectedExpression = expression;
    }

    /// Getters ///

    public List<String> getExpressions() {
        return expressions;
    }
    public String getSelectedExpression() {
        return selectedExpression;
    }

    /// Setters ///

    @Autowired
    public void setGlossaryWebClient(GlossaryWebClient glossaryWebClient) {
        this.glossaryWebClient = glossaryWebClient;
    }

    public void setSelectedExpression(String selectedExpression) {
        this.selectedExpression = selectedExpression;
    }
}
