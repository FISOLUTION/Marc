package fis.marc.service;

import fis.marc.domain.Process;
import fis.marc.domain.User;
import fis.marc.dto.SearchWorkerResponse;
import fis.marc.dto.UpdateUserRequest;
import fis.marc.repository.ProcessRepository;
import fis.marc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProcessRepository processRepository;

    @Transactional
    public void createUser(User user) {
        userRepository.save(user);
    }

    public List<SearchWorkerResponse> searchAllWorker() {
        List<SearchWorkerResponse> result = new ArrayList<>();
        List<User> allWorker = userRepository.findAllWorker();
        return getSearchUserResponses(result, allWorker);
    }

    private List<SearchWorkerResponse> getSearchUserResponses(List<SearchWorkerResponse> result, List<User> allWorker) {
        allWorker.forEach((worker) -> {
            String createDate = worker.getCreateDate();
            LocalDate parse = LocalDate.parse(createDate);
            Long betweenDays = ChronoUnit.DAYS.between(parse, LocalDate.now()) + 1;

            List<Process> progressingWork =
                    processRepository.findProgressingWorkByUserId(worker.getId(), worker.getAuth());

            int workingAmount = progressingWork.size();
            Long averageWorkingAmount = workingAmount / betweenDays;
            result.add(
                    new SearchWorkerResponse(worker.getId(), worker.getNickname(), worker.getPwd(),
                            worker.getAuth(), worker.getUsername(), worker.getAddress(),
                            worker.getPhnum(), workingAmount, averageWorkingAmount)
            );
        });
        return result;
    }

    @Transactional
    public void updateWorker(UpdateUserRequest ur) {
        User user = userRepository.findOne(ur.getUserId()).orElse(null);
        user.updateInfo(ur.getNickname(), ur.getPwd(), ur.getAuth(),
                ur.getUsername(), ur.getAddress(), ur.getPhnum());
    }

    @Transactional
    public void deleteWorker(Long userId) {
        User user = userRepository.findOne(userId).orElse(null);
        userRepository.delete(user);
    }


    public List<SearchWorkerResponse> searchAllChecker() {
        List<SearchWorkerResponse> result = new ArrayList<>();
        List<User> allWorker = userRepository.findAllChecker();
        return getSearchUserResponses(result, allWorker);
    }
}
