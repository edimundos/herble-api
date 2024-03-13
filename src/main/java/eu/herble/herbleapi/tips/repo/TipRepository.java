package eu.herble.herbleapi.tips.repo;

import eu.herble.herbleapi.instructions.model.Instruction;
import eu.herble.herbleapi.tips.model.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {
    List<Tip> findAll();

}
