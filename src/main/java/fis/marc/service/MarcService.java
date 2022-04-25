package fis.marc.service;

import fis.marc.domain.Marc;
import fis.marc.dto.InputMarcRequest;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.repository.MarcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarcService {

    private final MarcRepository marcRepository;

    @Transactional
    public void saveOrigin(SaveMarcRequest request) {
        request.getData().forEach(row -> {
            log.error(row.getMarc());
            Marc marc = Marc.createMarc(row.getMarc());
<<<<<<< HEAD
            marcRepository.saveOrigin(marc);
=======
               marcRepository.saveOrigin(marc);
>>>>>>> f198733e66f6d28674f387987d1c89f55056d340
        });
    }

    @Transactional
    public void saveWorked(Long id, String str) {
        Optional<Marc> marc = marcRepository.findOne(id);
        Marc marc1 = marc.get();
        marc1.updateWorked(str);
    }

    @Transactional
    public ParseOneResponse parseOne() {
        Marc oneOfAll = marcRepository.findOneOfAll();
        String contents = oneOfAll.getOrigin();
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
            if (Integer.parseInt(indicator) > 10) {
                char a;
                char b;
                if (str.charAt(0) == ' ' && str.charAt(1) == ' ') {
                    System.out.println("지시기호 = " + str.substring(0, 2));
                } else {
                    if (str.charAt(0) == ' ') {
                        a = '※';
                    } else {
                        a = str.charAt(0);
                    }
                    if (str.charAt(1) == ' ') {
                        b = '※';
                    } else {
                        b = str.charAt(1);
                    }
                    System.out.println("지시기호= " + a + b);
                }
            }
        }
        ParseOneResponse parseOneResponse = new ParseOneResponse(Leader, DirectoryList, DataList, field_length_List, field_start_List, indicator_List);
        return parseOneResponse;
    }

    @Transactional
    public String combine(InputMarcRequest inputMarcRequest) {
        int startPosition = 0;

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
        System.out.println("조합된 MARC 데이터 값 = " + (newDirectory.append(newData)));
        return (newDirectory.append(newData)).toString();
    }
}

