package fis.marc.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class UserServiceTest {
    
    @Test
    public void test() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.KOREA).dayOfWeek(), 1);
        LocalDate endOfWeek = today.with(WeekFields.of(Locale.KOREA).dayOfWeek(), 7);
    }
}