package fr.eql.akatz.demo.petpal.spring.back.service.impl;

import fr.eql.akatz.demo.petpal.spring.back.entity.Cat;
import fr.eql.akatz.demo.petpal.spring.back.entity.Owner;
import fr.eql.akatz.demo.petpal.spring.back.repository.CatDao;
import fr.eql.akatz.demo.petpal.spring.back.repository.OwnerDao;
import fr.eql.akatz.demo.petpal.spring.back.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryServiceImpl implements GalleryService {

    /** Injecté par le setter. */
    private CatDao catDao;
    /** Injecté par le setter. */
    private OwnerDao ownerDao;

    @Override
    public List<Cat> findOwnerCats(long id) {
        return catDao.findByOwnerId(id);
    }

    @Override
    public List<Owner> findAllOwnersButSelf(long id) {
        return ownerDao.findAllButSelf(id);
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
