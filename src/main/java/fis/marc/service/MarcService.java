package fis.marc.service;

import fis.marc.domain.Marc;
import fis.marc.domain.Process;
import fis.marc.domain.User;
import fis.marc.domain.enumType.Authority;
import fis.marc.domain.enumType.Status;
import fis.marc.dto.InputMarcRequest;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.exception.NoExistParsableException;
import fis.marc.repository.MarcRepository;
import fis.marc.repository.ProcessRepository;
import fis.marc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarcService {

    private final MarcRepository marcRepository;
    private final UserRepository userRepository;
    private final ProcessRepository processRepository;

    @Transactional
    public void saveOrigin(SaveMarcRequest request) {
        User user = userRepository.findOne(request.getUserId()).orElseGet(null);
        request.getData().forEach(row -> {
            log.error(row.getMarc());
            Marc marc = Marc.createMarc(row.getMarc());
            marcRepository.saveOrigin(marc);
            Process process = new Process(LocalDate.now().toString(), Status.Upload, marc, user);
            processRepository.saveProcess(process);
        });
    }

    @Transactional
    public void saveWorked(Long marcId, Long userId, String str) {
        Optional<Marc> findMarc = marcRepository.findOne(marcId);
        Optional<User> findUser = userRepository.findOne(userId);
        Marc marc = findMarc.orElseThrow(() -> new NoSuchElementException("존재하지 않는 Marc Id"));
        User user = findUser.orElseThrow(() -> new NoSuchElementException("존재하지 않는 User Id"));
        marc.updateWorked(str);
        List<Process> modifiesByMarc = processRepository.findModifyOneByMarc(marc);
        if (modifiesByMarc.isEmpty()) {
            processRepository.findWorkedOneByMarc(marc).ifPresentOrElse((process) -> {
                throw new IllegalStateException("수정요청 아닌 데이터는 다시 접근 및 저장 불가");
            }, () -> {
                Process process = new Process(LocalDate.now().toString(), Status.Input, marc, user);
                processRepository.saveProcess(process); // 저장되어있는 process 가 없으므로 새로 Input 상태 저장
            });
        } else {
            modifiesByMarc.forEach((process) -> {
                process.updateStatus(Status.Input); // 수정요청인 경우 Input 상태로 되돌림
            });
        }
    }

    @Transactional
    public void saveChecked(Long marcId, Long userId, String result, String comment) {
        Optional<Marc> findMarc = marcRepository.findOne(marcId);
        Optional<User> findUser = userRepository.findOne(userId);
        Marc marc = findMarc.orElseThrow(() -> new NoSuchElementException("존재하지 않는 Marc id"));
        User user = findUser.orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
        Optional<Process> findProcess = processRepository.findWorkedOneByMarc(marc);
        findProcess.orElseThrow(() -> new IllegalStateException("수정 요청한 데이터입니다. 수정이 완료되면 검수바랍니다."));
        findProcess.ifPresent((process) -> {
            Optional<Process> checkedOneByMarc = processRepository.findCheckedOneByMarc(marc);
            if (checkedOneByMarc.isPresent()) {
                throw new IllegalStateException("이미 검수 완료된 Marc 데이터입니다.");
            }
            processRepository.saveProcess(new Process(LocalDate.now().toString(), Status.Check, marc, user));
            marc.updateChecked(result);
            if (comment != null) {
                marc.updateComment(comment);
            }
        });
    }

    @Transactional
    public void updateModifyState(Long marcId, Long userId, String comment) {
        Marc marc = marcRepository.findOne(marcId).orElse(null);
        User user = userRepository.findOne(userId).orElse(null);

        marc.updateComment(comment);

        processRepository.findWorkedOneByMarc(marc).ifPresent((process -> {
            process.updateStatus(Status.Modify);
            process.updateChecker(user);
        })); // input 상태인 마크 데이터를 modify 상태로 바꾸고 checker 정보를 업데이트

    }

    public ParseOneResponse parseOriginOne() {
        Marc oneOfAll = marcRepository.findOneOriginRandom()
                .orElseThrow(() -> new NoExistParsableException("파싱할 origin 데이터가 없음"));
        ParseOneResponse response = parse(oneOfAll.getOrigin());
        response.setComment(oneOfAll.getComment()); // comment 있으면 보여줌
        return response;
    }

    public ParseOneResponse findParseOriginOne(Long id) {
        Marc marc = marcRepository.findOne(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 marc 아이디"));
        ParseOneResponse response = parse(marc.getOrigin());
        response.setComment(marc.getComment());
        return response;
    }

    public ParseOneResponse parseCheckedOne() {

        Marc oneOfAll = marcRepository.findOneCheckedRandom()
                .orElseThrow(() -> new NoExistParsableException("파싱할 worked 데이터가 없음"));
        ParseOneResponse response = parse(oneOfAll.getWorked());
        response.setComment(oneOfAll.getComment());
        return response;
    }

    public ParseOneResponse findParseCheckedOne(Long id) {
        Optional<Marc> findMarc = marcRepository.findOne(id);
        Marc marc = findMarc.orElseThrow(() -> new NoSuchElementException("존재하지 않는 marc 아이디"));
        if (marc.getWorked() == null) {
            throw new NoExistParsableException("검수페이지에서 입력이 안 된 marc 데이터를 조회");
        }
        if (marc.getChecked() == null) {
            return parse(marc.getWorked());
        } else {
            return parse(marc.getChecked());
        }
    }

    private ParseOneResponse parse(String contents) {
        byte[] contentsBytes = contents.getBytes();
        int leaderBytes = 24;

        String Leader = new String(contentsBytes, 0, leaderBytes); // 0~23 -> 24바이트가 리더
        int directoryStart = 24;
        int dataStart = Integer.parseInt(new String(contentsBytes, 12, 5)); // 13~17 바이트까지 Data의 시작 위치

        String Directory = new String(contentsBytes, directoryStart, dataStart-directoryStart); // 25~312
        String Data = new String(contentsBytes, dataStart, contentsBytes.length-dataStart); // 313~636

        List<String> DirectoryList = new ArrayList<>();
        List<String> DataList = new ArrayList<>();
        List<Integer> field_length_List = new ArrayList<>();
        List<Integer> field_start_List = new ArrayList<>();
        List<String> indicator_List = new ArrayList<>();

        for (int i = 0; i < dataStart-directoryStart-1; i+=12) {
            String indicator = Directory.substring(i, i + 3);
            int field_length = Integer.parseInt(Directory.substring(i + 3, i + 7));
            int field_start = Integer.parseInt(Directory.substring(i + 7, i + 12));
            String str = null;
            try {
                str = new String(Data.getBytes(), field_start, field_length);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
                DataList.clear();
                DataList.add(Data);
                break;
            }
            DirectoryList.add(Directory.substring(i, i + 12));
            DataList.add(str);
            field_length_List.add(field_length);
            field_start_List.add(field_start);
            indicator_List.add(indicator);
        }
        ParseOneResponse parseOneResponse = new ParseOneResponse(Leader, DirectoryList, DataList, field_length_List, field_start_List, indicator_List, null);
        return parseOneResponse;
    }

    public String combine(InputMarcRequest inputMarcRequest) {
        int startPosition = 0;
        Optional<Marc> one = marcRepository.findOne(inputMarcRequest.getMarc_id());
        Marc marc = one.orElseGet(null);
        byte[] contentsBytes = marc.getOrigin().getBytes();
        int leaderBytes = 24;
        String leader = new String(contentsBytes, 0, leaderBytes);
        StringBuilder Leader = new StringBuilder(leader);
        List<InputMarcRequest.InputMarcDto> dataList = inputMarcRequest.getDataList();
        StringBuilder newDirectory = new StringBuilder();
        StringBuilder newData = new StringBuilder();

        for (InputMarcRequest.InputMarcDto inputMarcDto : dataList) {
            String data = inputMarcDto.getData();
            int field_length = data.getBytes().length;

            newDirectory.append(inputMarcDto.getIndicator()).
                    append(String.format("%04d", field_length)).
                    append(String.format("%05d", startPosition));

            newData.append(data);

            startPosition += field_length;
        }
        newDirectory.append("\u001e");
        return ((Leader.append(newDirectory.append(newData))).append("\u001d")).toString();
    }
}

