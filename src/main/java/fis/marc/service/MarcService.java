package fis.marc.service;

import fis.marc.domain.Marc;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.repository.MarcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
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
        String contents = oneOfAll.getContent();
        byte[] contentsBytes = contents.getBytes();
        int leaderBytes = 24;

        String Leader = new String(contentsBytes, 0, leaderBytes); // 0~23 -> 24바이트가 리더
        int directoryStart = 24;
        int dataStart = Integer.parseInt(new String(contentsBytes, 12, 5)); // 13~17 바이트까지 Data의 시작 위치

//        String Directory = new String(contentsBytes, directoryStart, dataStart-directoryStart+2);
//        String Data = new String(contentsBytes, dataStart+2, contentsBytes.length-dataStart-2);

        String Directory = new String(contentsBytes, directoryStart, dataStart - directoryStart - 1); // 25~312
        String Data = new String(contentsBytes, dataStart - 1, contentsBytes.length - dataStart + 1); // 313~636

        ParseOneResponse parseOneResponse = new ParseOneResponse(Leader, Directory, Data);
        System.out.println("dataStart = " + dataStart);
        System.out.println("contentsBytes.length = " + contentsBytes.length);
        for (int i = 0; i < dataStart - directoryStart - 1; i += 12) {
            int field_length = Integer.parseInt(Directory.substring(i + 3, i + 7));
            int field_start = Integer.parseInt(Directory.substring(i + 7, i + 12));
//            String str = new String(Data.getBytes(), field_start, field_length);
//            System.out.println("================================================");
//            System.out.println("디렉토리개별값 = " + Directory.substring(i, i + 12) + "|| DATA 파싱값 = " + str);
        }
        return parseOneResponse;
    }

    public void test() {
        byte[] test = "뷁".getBytes(StandardCharsets.UTF_8);
        System.out.println(test.length);
        System.out.println("=====================================================");
        List<Marc> marcs = marcRepository.findAll();
        marcs.forEach(marc -> {
            byte[] t = marc.getContent().getBytes(StandardCharsets.UTF_8);
            System.out.println(t.length);
            int dataStart = Integer.parseInt(new String(t, 12, 5));
            int directoryStart = 24;
            String leader = new String(t, 0, 24);
            int totalLength = 0;
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            for (int i = 0; i < dataStart - directoryStart - 1; i += 12) {
                try{
                    String subDirectory = new String(t, directoryStart + i, 12);
                    System.out.println(subDirectory);
                    int fieldLength = Integer.parseInt(subDirectory.substring(3, 7));
                    int fieldStart = Integer.parseInt(subDirectory.substring(7, 12));
                    String subData = new String(t, dataStart + fieldStart, fieldLength);
                    System.out.println(subData);
                    System.out.println(subData.getBytes(StandardCharsets.UTF_8).length);
                    totalLength += subData.getBytes(StandardCharsets.UTF_8).length;
                    System.out.println(totalLength);
                } catch (StringIndexOutOfBoundsException ex){
                    log.warn(ex.getMessage());
                }
            }
            System.out.println(totalLength);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        });
    }
}
