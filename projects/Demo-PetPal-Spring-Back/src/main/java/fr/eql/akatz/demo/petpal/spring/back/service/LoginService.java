package fr.eql.akatz.demo.petpal.spring.back.service;

import fr.eql.akatz.demo.petpal.spring.back.entity.Owner;

public interface LoginService {

	Owner authenticate(String login, String password);

}
