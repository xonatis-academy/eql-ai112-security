package fr.eql.akatz.demo.petpal.spring.back.repository;

import fr.eql.akatz.demo.petpal.spring.back.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatDao extends JpaRepository<Cat, Long> {

    List<Cat> findByOwnerId(long id);
}
