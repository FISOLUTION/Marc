package fis.marc.service;

import fis.marc.domain.Marc;
import fis.marc.domain.Process;
import fis.marc.domain.User;
import fis.marc.domain.enumType.Status;
import fis.marc.dto.InputMarcRequest;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
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
        Marc marc = findMarc.orElse(null);
        marc.updateWorked(str);
        Process process = new Process(LocalDate.now().toString(), Status.Input, marc, findUser.orElse(null));
        processRepository.saveProcess(process);
    }

    @Transactional
    public void saveChecked(Long marcId, Long userId, String result, String comment) {
        Optional<Marc> findMarc = marcRepository.findOne(marcId);
        Optional<User> findUser = userRepository.findOne(userId);
        Marc marc = findMarc.orElse(null);
        marc.updateChecked(result);
        marc.updateComment(comment);
        Process process = new Process(LocalDate.now().toString(), Status.Check, marc, findUser.orElse(null));
        processRepository.saveProcess(process);
    }

    public ParseOneResponse parseOriginOne() {
        Marc oneOfAll = marcRepository.findOneOriginRandom();
        return parse(oneOfAll.getOrigin());
    }

    public ParseOneResponse findParseOriginOne(Long id) {
        Optional<Marc> findMarc = marcRepository.findOne(id);
        Marc marc = findMarc.orElseThrow(() -> new NoSuchElementException("존재하지 않는 marc 아이디"));
        return parse(marc.getOrigin());
    }

    public ParseOneResponse parseCheckedOne() {
        Marc oneOfAll = marcRepository.findOneCheckedRandom();
        return parse(oneOfAll.getWorked());
    }

    public ParseOneResponse findParseCheckedOne(Long id) {
        Optional<Marc> findMarc = marcRepository.findOne(id);
        Marc marc = findMarc.orElseThrow(() -> new NoSuchElementException("존재하지 않는 marc 아이디"));
        return parse(marc.getWorked());
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

        System.out.println("dataStart = " + dataStart);
        System.out.println("contentsBytes.length = " + contentsBytes.length);
        System.out.println("Leader.getBytes().length = " + Leader.getBytes().length);
        System.out.println("Directory.getBytes().length = " + Directory.getBytes().length);
        System.out.println("Data.getBytes().length = " + Data.getBytes().length);
        for (int i = 0; i < dataStart-directoryStart-1; i+=12) {
            String indicator = Directory.substring(i, i + 3);
            int field_length = Integer.parseInt(Directory.substring(i + 3, i + 7));
            int field_start = Integer.parseInt(Directory.substring(i + 7, i + 12));
            String str = new String(Data.getBytes(), field_start, field_length);
            System.out.println("================================================");
            System.out.println("디렉토리개별값 = " + Directory.substring(i, i + 12));
            System.out.println("DATA 파싱값 = " + str);
            DirectoryList.add(Directory.substring(i, i + 12));
            DataList.add(str);
            field_length_List.add(field_length);
            field_start_List.add(field_start);
            indicator_List.add(indicator);
        }
        ParseOneResponse parseOneResponse = new ParseOneResponse(Leader, DirectoryList, DataList, field_length_List, field_start_List, indicator_List);
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
            System.out.println("inputMarcDto.getData() = " + inputMarcDto.getData());
            String data = inputMarcDto.getData();
            int field_length = data.getBytes().length;
            System.out.println("디렉토리개별값 = " + inputMarcDto.getIndicator()
                    + String.format("%04d", field_length)
                    + String.format("%05d", startPosition));

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

