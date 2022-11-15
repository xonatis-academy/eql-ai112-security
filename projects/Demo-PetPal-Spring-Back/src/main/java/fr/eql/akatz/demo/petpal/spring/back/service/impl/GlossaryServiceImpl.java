package fr.eql.akatz.demo.petpal.spring.back.service.impl;

import fr.eql.akatz.demo.petpal.spring.back.entity.dto.WikiExtract;
import fr.eql.akatz.demo.petpal.spring.back.repository.GlossaryDao;
import fr.eql.akatz.demo.petpal.spring.back.service.GlossaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GlossaryServiceImpl implements GlossaryService {

	/** Injecté par le setter. */
	private GlossaryDao glossaryDao;

	private static final String WIKI_REQUEST_FIRST_PART = "https://fr.wikipedia.org/w/api.php?action=query&titles=";
	private static final String WIKI_REQUEST_SECOND_PART = "&prop=extracts&exchars=500&explaintext&utf8&format=json";

	private final RestTemplate restTemplate;

	// Constructeur à appeler par défaut.
	public GlossaryServiceImpl() {
		this.restTemplate = new RestTemplate();
	}

	// Constructeur à appeler pour les tests (afin de mocker le restTemplate).
	public GlossaryServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<String> findGlossaryExpressions() {
		return glossaryDao.findAllExpressions();
	}

	@Override
	public String fetchExtract(String expression){
		WikiExtract wikiExtract = restTemplate.getForObject(
				WIKI_REQUEST_FIRST_PART + expression + WIKI_REQUEST_SECOND_PART,
				WikiExtract.class
		);
		if (wikiExtract == null) {
			return "Une erreur s'est produite lors de l'appel Rest vers l'API Wikipédia.";
		}
		if (wikiExtract.getQuery().getPages().getPage().get("-1") != null) {
			return "Expression inconnue.";
		}
		Map<String, Map<String, Object>> page = wikiExtract.getQuery().getPages().getPage();
		Set<String> pageIdKeys = page.keySet();
		String pageIdKey = "";
		for (String key : pageIdKeys) {
			pageIdKey = key;
		}
		String extract = (String) page.get(pageIdKey).get("extract");
		if (extract.equals("")) {
			return "Aucun extrait disponible pour cette expression.";
		}
		return extract;
	}

	/// Setters ///

	@Autowired
	public void setGlossaryDao(GlossaryDao glossaryDao) {
		this.glossaryDao = glossaryDao;
	}
}
