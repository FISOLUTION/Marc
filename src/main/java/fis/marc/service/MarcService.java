package fis.marc.service;

import fis.marc.domain.Marc;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.repository.MarcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarcService {

    private final MarcRepository marcRepository;

    @Transactional
    public void saveMarc(SaveMarcRequest request) {
        request.getData().forEach(row -> {
            log.error(row.getMarc());
            Marc marc = Marc.createMarc(row.getMarc());
                marcRepository.save(marc);
        });
    }
}
