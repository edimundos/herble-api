package eu.herble.herbleapi.instructions;

import eu.herble.herbleapi.instructions.data.InstructionsModel;
import eu.herble.herbleapi.instructions.model.Instruction;
import eu.herble.herbleapi.instructions.service.InstructionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/instructions")
public class InstructionController {

    @Autowired
    private InstructionService instructionService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Instruction>> getAllInstructions() {
        List<Instruction> instructions = instructionService.getAllInstructions();
        return ResponseEntity.ok().body(instructions);
    }

    @PostMapping("/create")
    public ResponseEntity<Instruction> createInstruction(@RequestBody InstructionsModel instructionsModel) {
        Instruction savedInstruction = instructionService.createInstruction(instructionsModel);
        return new ResponseEntity<>(savedInstruction, HttpStatus.CREATED);
    }

}
