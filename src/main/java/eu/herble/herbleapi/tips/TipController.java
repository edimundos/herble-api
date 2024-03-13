package eu.herble.herbleapi.tips;

import eu.herble.herbleapi.tips.data.TipsModel;
import eu.herble.herbleapi.tips.model.Tip;
import eu.herble.herbleapi.tips.service.TipService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/api/tips")
public class TipController {

    @Autowired
    private TipService tipService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Tip>> getAllTips() {
        List<Tip> tips = tipService.getAllTips();
        return ResponseEntity.ok().body(tips);
    }

    @PostMapping("/create")
    public ResponseEntity<Tip> createTip(@RequestBody TipsModel tipsModel) {
        Tip savedTip = tipService.createTip(tipsModel);
        return new ResponseEntity<>(savedTip, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
        }

        try {
            // Here, implement your logic to handle the file. For example, saving it to disk or a database.
            String fileName = file.getOriginalFilename();
            // Save the file...

            return ResponseEntity.ok("You successfully uploaded " + fileName + "!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file: " + file.getOriginalFilename() + "!");
        }
    }

}
