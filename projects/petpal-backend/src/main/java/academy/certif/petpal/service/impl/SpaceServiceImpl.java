package academy.certif.petpal.service.impl;

import academy.certif.petpal.entity.Cat;
import academy.certif.petpal.entity.Owner;
import academy.certif.petpal.entity.dto.CatDto;
import academy.certif.petpal.repository.CatDao;
import academy.certif.petpal.repository.OwnerDao;
import academy.certif.petpal.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceServiceImpl implements SpaceService {

    /** Injecté par le setter. */
    CatDao catDao;
    /** Injecté par le setter. */
    OwnerDao ownerDao;

    @Override
    public List<Cat> findOwnerCats(long id) {
        return catDao.findByOwnerId(id);
    }

    @Override
    public Cat saveCat(CatDto catDto) {
        Owner owner = ownerDao.findById(catDto.getOwnerId());
        Cat cat = new Cat(catDto.getName(), catDto.getBreed(), catDto.getBirthDate(), catDto.getPicture(), owner);
        catDao.save(cat);
        return cat;
    }

    /// Setters ///

    @Autowired
    public void setCatDao(CatDao catDao) {
        this.catDao = catDao;
    }
    @Autowired
    public void setOwnerDao(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }
}
