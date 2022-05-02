package fis.marc.service;

import fis.marc.domain.Manage;
import fis.marc.repository.ManageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManageService {
    private final ManageRepository manageRepository;

    public void saveGoal(Long goal) {
        manageRepository.findAll().ifPresentOrElse((manage) -> {
            manage.updateGoal(goal);
        }, () -> {
            manageRepository.saveManage(new Manage(goal));
        });
    }

}
