package fr.eql.akatz.demo.petpal.spring.back.service.impl;

import fr.eql.akatz.demo.petpal.spring.back.entity.Owner;
import fr.eql.akatz.demo.petpal.spring.back.repository.OwnerDao;
import fr.eql.akatz.demo.petpal.spring.back.service.LoginService;
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
