package academy.certif.petpal.repository;

import academy.certif.petpal.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatDao extends JpaRepository<Cat, Long> {

    List<Cat> findByOwnerId(long id);
}
