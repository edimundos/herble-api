package eu.herble.herbleapi.instructions.repo;

import eu.herble.herbleapi.instructions.model.Instruction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long>{
    List<Instruction> findAll();
}
