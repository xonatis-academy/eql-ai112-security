package academy.certif.petpal.service.impl;

import academy.certif.petpal.entity.Cat;
import academy.certif.petpal.entity.Owner;
import academy.certif.petpal.repository.CatDao;
import academy.certif.petpal.repository.OwnerDao;
import academy.certif.petpal.service.GalleryService;
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
