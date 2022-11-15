package fr.eql.akatz.demo.petpal.spring.back.repository;

import fr.eql.akatz.demo.petpal.spring.back.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OwnerDao extends JpaRepository<Owner, Long> {

    Owner findById(long id);

    @Query("SELECT o FROM Owner o JOIN FETCH o.favoritePetCategories WHERE o.login = :login AND o.password = :password")
    Owner findByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    @Query("SELECT DISTINCT o FROM Owner o JOIN FETCH o.favoritePetCategories WHERE o.id != :id")
    List<Owner> findAllButSelf(@Param("id") long id);
}
