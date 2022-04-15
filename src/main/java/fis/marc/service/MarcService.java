package fis.marc.service;

import fis.marc.domain.Marc;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.repository.MarcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


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

    @Transactional
    public ParseOneResponse parseOne() {
        Marc oneOfAll = marcRepository.findOneOfAll();
        String contents = oneOfAll.getOrigin();
        byte[] contentsBytes = contents.getBytes();
        int leaderBytes = 24;

        String Leader = new String(contentsBytes, 0, leaderBytes); // 0~23 -> 24바이트가 리더
        int directoryStart = 24;
        int dataStart = Integer.parseInt(new String(contentsBytes, 12, 5)); // 13~17 바이트까지 Data의 시작 위치

//        String Directory = new String(contentsBytes, directoryStart, dataStart-directoryStart+2);
//        String Data = new String(contentsBytes, dataStart+2, contentsBytes.length-dataStart-2);

        String Directory = new String(contentsBytes, directoryStart, dataStart-directoryStart); // 25~312
        String Data = new String(contentsBytes, dataStart, contentsBytes.length-dataStart); // 313~636

        List<String> DirectoryList = new ArrayList<>();
        List<String> DataList = new ArrayList<>();

        System.out.println("dataStart = " + dataStart);
        System.out.println("contentsBytes.length = " + contentsBytes.length);
        System.out.println("Leader.getBytes().length = " + Leader.getBytes().length);
        System.out.println("Directory.getBytes().length = " + Directory.getBytes().length);
        System.out.println("Data.getBytes().length = " + Data.getBytes().length);
        for (int i = 0; i < dataStart-directoryStart-1; i+=12) {
            int field_length = Integer.parseInt(Directory.substring(i + 3, i + 7));
            int field_start = Integer.parseInt(Directory.substring(i + 7, i + 12));
            String str = new String(Data.getBytes(), field_start, field_length);
            System.out.println("================================================");
            System.out.println("디렉토리개별값 = " + Directory.substring(i, i + 12));
            System.out.println("DATA 파싱값 = " + str);
            DirectoryList.add(Directory.substring(i, i + 12));
            DataList.add(str);
        }
        ParseOneResponse parseOneResponse = new ParseOneResponse(Leader, DirectoryList, DataList);
        return parseOneResponse;
    }
}
