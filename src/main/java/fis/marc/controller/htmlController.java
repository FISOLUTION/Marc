package fis.marc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

@Controller
@Slf4j
@RequiredArgsConstructor
public class htmlController {

    @GetMapping("")
    public String index(Model model) throws FileNotFoundException {
        System.out.println("\u001d"+"@@@@@@@@@@");
        Scanner scanner = new Scanner("/Users/seungbumlee/Downloads/marc2.txt");
        while (scanner.hasNext()) {
            String str = scanner.next("\\u001D");
            System.out.println(str);
        }
        return "home";
    }
}