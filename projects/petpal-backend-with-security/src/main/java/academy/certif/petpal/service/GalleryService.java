package academy.certif.petpal.service;

import academy.certif.petpal.entity.Cat;
import academy.certif.petpal.entity.Owner;

import java.util.List;

public interface GalleryService {

	List<Cat> findOwnerCats(long id);
	List<Owner> findAllOwnersButSelf(long id);
}
