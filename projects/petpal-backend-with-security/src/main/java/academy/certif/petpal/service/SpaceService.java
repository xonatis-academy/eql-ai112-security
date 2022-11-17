package academy.certif.petpal.service;

import academy.certif.petpal.entity.Cat;
import academy.certif.petpal.entity.dto.CatDto;

import java.util.List;

public interface SpaceService {

    List<Cat> findOwnerCats(long id);
    Cat saveCat(CatDto catDto);
}
