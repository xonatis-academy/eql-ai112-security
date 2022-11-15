package fr.eql.akatz.demo.petpal.spring.back.service.impl;

import fr.eql.akatz.demo.petpal.spring.back.entity.dto.Pages;
import fr.eql.akatz.demo.petpal.spring.back.entity.dto.Query;
import fr.eql.akatz.demo.petpal.spring.back.entity.dto.WikiExtract;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class GlossaryServiceImplTest {

    private static final String WIKI_REQUEST_FIRST_PART = "https://fr.wikipedia.org/w/api.php?action=query&titles=";
    private static final String WIKI_REQUEST_SECOND_PART = "&prop=extracts&exchars=500&explaintext&utf8&format=json";

    private static final String UNKNOWN_EXPRESSION = "sdvdsdsvz";
    private static final String NO_EXTRACT_EXPRESSION = "toto";
    private static final String CORRECT_EXPRESSION = "table";

    private static final String WIKI_UNKNOWN_EXPRESSION_REQUEST =
            WIKI_REQUEST_FIRST_PART + UNKNOWN_EXPRESSION + WIKI_REQUEST_SECOND_PART;
    private static final String WIKI_NO_EXTRACT_EXPRESSION_REQUEST =
            WIKI_REQUEST_FIRST_PART + NO_EXTRACT_EXPRESSION + WIKI_REQUEST_SECOND_PART;
    private static final String WIKI_CORRECT_EXPRESSION_REQUEST =
            WIKI_REQUEST_FIRST_PART + CORRECT_EXPRESSION + WIKI_REQUEST_SECOND_PART;

    private static final String REST_ISSUE_EXPECTED_RETURN =
            "Une erreur s'est produite lors de l'appel Rest vers l'API Wikipédia.";
    private static final String UNKNOWN_EXPRESSION_EXPECTED_RETURN =
            "Expression inconnue.";
    private static final String NO_EXTRACT_EXPRESSION_EXPECTED_RETURN =
            "Aucun extrait disponible pour cette expression.";
    private static final String CORRECT_EXPRESSION_EXPECTED_RETURN =
            "La table est un type de meuble composé d'une surface plane et horizontale...";

    /** Instancié dans le le setUp() avant chaque test. */
    private GlossaryServiceImpl testee;

    @Mock
    private RestTemplate restTemplateMock;

    /**
     * J'instancie l'objet (GlossaryServiceImpl) contenant les méthodes à tester, en utilisant
     * le constructeur permettant de donner en paramètre le mock de son collaborateur.
     */
    @BeforeEach
    void setUp() {
        testee = new GlossaryServiceImpl(restTemplateMock);
    }

    @Test
    void fetchExtractWithRestIssueTest() {
        Mockito.when(restTemplateMock.getForObject(WIKI_CORRECT_EXPRESSION_REQUEST, WikiExtract.class)).thenReturn(null);
        String actual = testee.fetchExtract(CORRECT_EXPRESSION);
        Assertions.assertEquals(REST_ISSUE_EXPECTED_RETURN, actual);
    }

    @Test
    void fetchExtractWithUnknownExpressionTest() {
        Map<String, Map<String,Object>> page = new HashMap<>();
        page.put("-1", new HashMap<>());
        Pages pages = new Pages(page);
        Query query = new Query(pages);
        WikiExtract wikiExtract = new WikiExtract(query);
        Mockito.when(restTemplateMock.getForObject(WIKI_UNKNOWN_EXPRESSION_REQUEST, WikiExtract.class)).thenReturn(wikiExtract);
        String actual = testee.fetchExtract(UNKNOWN_EXPRESSION);
        Assertions.assertEquals(UNKNOWN_EXPRESSION_EXPECTED_RETURN, actual);
    }

    @Test
    void fetchExtractWithNoExtractExpressionTest() {
        Map<String,Object> pageContent = new HashMap<>();
        pageContent.put("extract", "");
        Map<String, Map<String,Object>> page = new HashMap<>();
        page.put("12345", pageContent);
        Pages pages = new Pages(page);
        Query query = new Query(pages);
        WikiExtract wikiExtract = new WikiExtract(query);
        Mockito.when(restTemplateMock.getForObject(WIKI_NO_EXTRACT_EXPRESSION_REQUEST, WikiExtract.class)).thenReturn(wikiExtract);
        String actual = testee.fetchExtract(NO_EXTRACT_EXPRESSION);
        Assertions.assertEquals(NO_EXTRACT_EXPRESSION_EXPECTED_RETURN, actual);
    }

    @Test
    void fetchExtractWithCorrectExpressionTest() {
        Map<String,Object> pageContent = new HashMap<>();
        pageContent.put("extract", CORRECT_EXPRESSION_EXPECTED_RETURN);
        Map<String, Map<String,Object>> page = new HashMap<>();
        page.put("12345", pageContent);
        Pages pages = new Pages(page);
        Query query = new Query(pages);
        WikiExtract wikiExtract = new WikiExtract(query);
        Mockito.when(restTemplateMock.getForObject(WIKI_CORRECT_EXPRESSION_REQUEST, WikiExtract.class)).thenReturn(wikiExtract);
        String actual = testee.fetchExtract(CORRECT_EXPRESSION);
        Assertions.assertEquals(CORRECT_EXPRESSION_EXPECTED_RETURN, actual);
    }
}