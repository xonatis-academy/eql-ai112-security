package academy.certif.petpal.service;

import academy.certif.petpal.entity.Owner;

public interface LoginService {

	Owner authenticate(String login, String password);

}
