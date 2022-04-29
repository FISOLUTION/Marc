package fis.marc.service;

import fis.marc.domain.Process;
import fis.marc.domain.User;
import fis.marc.domain.enumType.Authority;
import fis.marc.domain.enumType.Status;
import fis.marc.dto.SearchWorkerResponse;
import fis.marc.dto.StatisticsResponse;
import fis.marc.dto.StatisticsResponse.WorkerInfo.WorkerInfoDto;
import fis.marc.dto.StatisticsResponse.WorkerInfo.WorkerInfoMonthDto;
import fis.marc.dto.StatisticsResponse.WorkingAmount.Performance;
import fis.marc.dto.StatisticsResponse.WorkingAmount.WorkingAmountDto;
import fis.marc.dto.StatisticsResponse.WorkingAmount.WorkingAmountMonthDto;
import fis.marc.dto.UpdateUserRequest;
import fis.marc.exception.DuplicateNicknameException;
import fis.marc.repository.ProcessRepository;
import fis.marc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProcessRepository processRepository;

    @Transactional
    public void createUser(User user) {
        if (userRepository.findOneByNickname(user.getNickname()).isPresent()) {
            throw new DuplicateNicknameException("중복된 회원 닉네임");
        } else {
            userRepository.save(user);
        }
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
                    processRepository.findWorkingAmountByUserId(worker.getId());

            int rejectedAmount = processRepository.findModifyAllByWorker(worker).size();
            int workingAmount = progressingWork.size();
            Long averageWorkingAmount = workingAmount / betweenDays;
            if (workingAmount == 0) workingAmount = 1;
            Double rejectedRate = (1.0 * rejectedAmount / workingAmount) * 100;
            System.out.println("rejectedAmount = " + rejectedAmount);

            SearchWorkerResponse searchWorkerResponse = new SearchWorkerResponse(worker.getId(), worker.getNickname(), worker.getPwd(),
                    worker.getAuth(), worker.getUsername(), worker.getAddress(),
                    worker.getPhnum(), workingAmount, averageWorkingAmount, rejectedRate);
            Map<String, List<Process>> processMap = progressingWork.stream()
                    .collect(Collectors.groupingBy(Process::getCreatedDate));
            processMap.forEach((key, value) -> {
                Integer amount = value.size();
                searchWorkerResponse.getTendency()
                        .add(new SearchWorkerResponse.TendencyDto(key, amount));
            });
            result.add(searchWorkerResponse);
        });
        return result;
    } // 유저 목록 조회

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

    public StatisticsResponse findAllWorkerByDate() {
        StatisticsResponse statisticsResponse = new StatisticsResponse(new StatisticsResponse.WorkerInfo(), new StatisticsResponse.WorkingAmount());
        LocalDate now = LocalDate.now().minusDays(4);
        LocalDate month = LocalDate.now().minusMonths(4);
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.KOREA).dayOfWeek(), 1).minusWeeks(4);
        LocalDate endOfWeek = today.with(WeekFields.of(Locale.KOREA).dayOfWeek(), 7).minusWeeks(4);

        int numOfWorkerByDate = 0;
        int numOfCheckerByDate = 0;
        int numOfWorkerByWeek = 0;
        int numOfCheckerByWeek = 0;
        int numOfWorkerByMonth = 0;
        int numOfCheckerByMonth = 0;
        int accumInputSize = 0;
        int accumCheckSize = 0;

        for (int i = 0; i < 5; i++) {
            numOfWorkerByDate += userRepository.findAllUserByDate(now.toString(), Authority.Worker).size();
            numOfCheckerByDate += userRepository.findAllUserByDate(now.toString(), Authority.Checker).size();
            addDateInfoEach(statisticsResponse, now, numOfWorkerByDate, numOfCheckerByDate);
            now = now.plusDays(1);

            numOfWorkerByWeek += userRepository.findAllUserByWeek(startOfWeek.toString(), endOfWeek.toString(), Authority.Worker).size();
            numOfCheckerByWeek += userRepository.findAllUserByWeek(startOfWeek.toString(), endOfWeek.toString(), Authority.Checker).size();
            addWeekInfoEach(statisticsResponse, numOfWorkerByWeek, numOfCheckerByWeek, startOfWeek, endOfWeek);
            startOfWeek = startOfWeek.plusWeeks(1);
            endOfWeek = endOfWeek.plusWeeks(1);

            Integer monthValue = month.getMonthValue();
            numOfWorkerByMonth += userRepository.findAllUserByMonth(monthValue, Authority.Worker).size();
            numOfCheckerByMonth += userRepository.findAllUserByMonth(monthValue, Authority.Checker).size();
            List<Process> inputsByMonth = processRepository.findAllProcessByMonth(monthValue, Status.Input);
            List<Process> checksByMonth = processRepository.findAllProcessByMonth(monthValue, Status.Check);
            addMonthInfoEach(statisticsResponse, numOfWorkerByMonth, numOfCheckerByMonth, monthValue, inputsByMonth, checksByMonth);

            accumInputSize += inputsByMonth.size();
            accumCheckSize += checksByMonth.size();
            statisticsResponse.getWorkingAmount().getAccumulation()
                    .add(new WorkingAmountMonthDto(monthValue.toString(), accumInputSize, accumCheckSize));

            month = month.plusMonths(1);
        }

        addPerformanceInfo(statisticsResponse);

        return statisticsResponse;
    }

    private void addPerformanceInfo(StatisticsResponse statisticsResponse) {
        int total = processRepository.findCheckedAll().size();
        int goal = 50000;
        int workingDay = 10;

        double averageAmount = (total * 1.0 / workingDay);

        int expectedWorkingDay;
        if (averageAmount == 0) {
            expectedWorkingDay = 999999999; // 목표량/하루평균 작업량 -> 총 예상 작업 일수
        } else {
            expectedWorkingDay = (int)(goal / averageAmount); // 목표량/하루평균 작업량 -> 총 예상 작업 일수
        }

        int expectedRemainWorkingDay = expectedWorkingDay - workingDay;// 목표량/하루평균 작업량 -> 총 예상 남은 작업 일수
        String expectedFinishDate = LocalDate.now().plusDays(expectedRemainWorkingDay).toString();

        statisticsResponse.getWorkingAmount()
                .setPerformance(new Performance(total, goal, workingDay, expectedWorkingDay, expectedFinishDate));
    }

    private void addMonthInfoEach(StatisticsResponse statisticsResponse, int numOfWorkerByMonth, int numOfCheckerByMonth, Integer monthValue, List<Process> inputsByMonth, List<Process> checksByMonth) {
        statisticsResponse.getWorkerInfo().getMonth()
                .add(new WorkerInfoMonthDto(monthValue.toString(), numOfWorkerByMonth, numOfCheckerByMonth));
        statisticsResponse.getWorkingAmount().getMonth()
                .add(new WorkingAmountMonthDto(monthValue.toString(), inputsByMonth.size(), checksByMonth.size()));
    }

    private void addWeekInfoEach(StatisticsResponse statisticsResponse, int numOfWorkerByWeek, int numOfCheckerByWeek, LocalDate startOfWeek, LocalDate endOfWeek) {
        statisticsResponse.getWorkerInfo().getWeek()
                .add(new WorkerInfoDto(endOfWeek.toString(), numOfWorkerByWeek, numOfCheckerByWeek));
        List<Process> inputsByWeek = processRepository.findAllProcessByWeek(startOfWeek.toString(), endOfWeek.toString(), Status.Input);
        List<Process> checksByWeek = processRepository.findAllProcessByWeek(startOfWeek.toString(), endOfWeek.toString(), Status.Check);
        statisticsResponse.getWorkingAmount().getWeek()
                .add(new WorkingAmountDto(startOfWeek.toString(), inputsByWeek.size(), checksByWeek.size()));
    }

    private void addDateInfoEach(StatisticsResponse statisticsResponse, LocalDate now, int numOfWorkerByDate, int numOfCheckerByDate) {
        statisticsResponse.getWorkerInfo().getDate()
                .add(new WorkerInfoDto(now.toString(), numOfWorkerByDate, numOfCheckerByDate));
        List<Process> inputs = processRepository.findAllProcessByDate(now.toString(), Status.Input);
        List<Process> checks = processRepository.findAllProcessByDate(now.toString(), Status.Check);
        statisticsResponse.getWorkingAmount().getDate()
                .add(new WorkingAmountDto(now.toString(), inputs.size(), checks.size()));
    }

    public User findUser(Long userId) {
        return userRepository.findOne(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저 아이디"));
    }
}
