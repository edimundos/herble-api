package eu.herble.herbleapi.tips.repo;

import eu.herble.herbleapi.tips.model.Tip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {
    List<Tip> findAll();

}
