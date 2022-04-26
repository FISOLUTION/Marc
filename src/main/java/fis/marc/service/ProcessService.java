package fis.marc.service;

import fis.marc.domain.Marc;
import fis.marc.domain.Process;
import fis.marc.domain.User;
import fis.marc.dto.ProcessStatusResponse;
import fis.marc.dto.WorkListResponse;
import fis.marc.repository.ProcessRepository;
import fis.marc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProcessService {

    private final ProcessRepository processRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkListResponse findAllByUserId(Long userId) {
        WorkListResponse workList = new WorkListResponse();
        List<Process> resultList = processRepository.findAllByUserId(userId);
        resultList.forEach((result) -> {
            Marc marc = result.getMarc();
            byte[] contentsBytes = marc.getOrigin().getBytes();
            int leaderBytes = 24;
            String Leader = new String(contentsBytes, 0, leaderBytes);

            workList.getWorkDtoList().add(
                    new WorkListResponse.WorkDto(result.getMarc().getId(), Leader,
                            result.getCreatedDate(), result.getStatus(), marc.getComment()));
        });
        return workList;
    }

    public ProcessStatusResponse findAllProcessStatus() {
        ProcessStatusResponse processStatusResponse = new ProcessStatusResponse(processRepository.totalSize(),
                processRepository.checkSize(), processRepository.InputSize());
        List<User> users = userRepository.findAll();

        users.forEach((user -> {
            String createDate = user.getCreateDate();
            LocalDate parse = LocalDate.parse(createDate);
            Long betweenDays = ChronoUnit.DAYS.between(parse, LocalDate.now()) + 1;
            List<Process> progressingWork =
                    processRepository.findProgressingWorkByUserId(user.getId(), user.getAuth());

            processStatusResponse.getPerformance().add(new ProcessStatusResponse.ProcessDto(
                    user.getUsername(), betweenDays.toString(), progressingWork.size(), user.getAuth()
            ));
        }));
        return processStatusResponse;
    }
}
