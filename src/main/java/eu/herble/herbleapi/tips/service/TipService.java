package eu.herble.herbleapi.tips.service;

import eu.herble.herbleapi.tips.data.TipsModel;
import eu.herble.herbleapi.tips.model.Tip;
import eu.herble.herbleapi.tips.repo.TipRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TipService {

    @Autowired
    private TipRepository tipRepository;

    public Tip createTip (TipsModel tipsModel) {
        Tip tip = new Tip();
        tip.setTitle(tipsModel.getTitle());
        tip.setDescription(tipsModel.getDescription());
        tip.setPicture(tipsModel.getPicture());
        tipRepository.save(tip);
        return tip;
    }

    public List<Tip> getAllTips() {
        return tipRepository.findAll();
    }
}
