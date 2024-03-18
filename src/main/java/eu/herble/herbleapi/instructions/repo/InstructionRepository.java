package eu.herble.herbleapi.instructions.repo;

import eu.herble.herbleapi.instructions.model.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long>{
    List<Instruction> findAll();
}
