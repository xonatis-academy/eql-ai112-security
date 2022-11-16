package academy.certif.petpal.service;

import java.util.List;

public interface GlossaryService {

    List<String> findGlossaryExpressions();
    String fetchExtract(String expression);
}
