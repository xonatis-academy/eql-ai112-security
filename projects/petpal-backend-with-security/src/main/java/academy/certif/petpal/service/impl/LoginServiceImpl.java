package academy.certif.petpal.service.impl;

import academy.certif.petpal.entity.Owner;
import academy.certif.petpal.repository.OwnerDao;
import academy.certif.petpal.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    /** Injecté par le setter. */
    private OwnerDao ownerDao;

    @Override
    public Owner authenticate(String login, String password) {
        return ownerDao.findByLoginAndPassword(login, password);
    }

    /// Setters ///

    @Autowired
    public void setOwnerDao(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }
}
