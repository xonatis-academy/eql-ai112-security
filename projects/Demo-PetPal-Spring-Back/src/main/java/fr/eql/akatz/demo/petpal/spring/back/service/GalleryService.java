package fr.eql.akatz.demo.petpal.spring.back.service;

import fr.eql.akatz.demo.petpal.spring.back.entity.Cat;
import fr.eql.akatz.demo.petpal.spring.back.entity.Owner;

import java.util.List;

public interface GalleryService {

	List<Cat> findOwnerCats(long id);
	List<Owner> findAllOwnersButSelf(long id);
}
