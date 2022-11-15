package fr.eql.akatz.demo.petpal.spring.back.service;

import java.util.List;

public interface GlossaryService {

    List<String> findGlossaryExpressions();
    String fetchExtract(String expression);
}
