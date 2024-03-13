package eu.herble.herbleapi.instructions.service;

import eu.herble.herbleapi.instructions.model.Instruction;
import eu.herble.herbleapi.instructions.data.InstructionsModel;
import eu.herble.herbleapi.instructions.repo.InstructionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InstructionService {

    @Autowired
    private InstructionRepository instructionRepository;

    public Instruction createInstruction (InstructionsModel instructionsModel) {
        Instruction instruction = new Instruction();
        instruction.setQuestion(instructionsModel.getQuestion());
        instruction.setAnswer(instructionsModel.getAnswer());
        instructionRepository.save(instruction);
        return instruction;
    }

    public List<Instruction> getAllInstructions() {
        return instructionRepository.findAll();
    }
}
