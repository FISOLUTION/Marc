package fis.marc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {

    private WorkerInfo workerInfo;
    private WorkingAmount workingAmount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkerInfo {
        private List<WorkerInfoDto> date = new ArrayList<>();
        private List<WorkerInfoDto> week = new ArrayList<>();
        private List<WorkerInfoMonthDto> month = new ArrayList<>();

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WorkerInfoDto {
            private String date;
            private int worker;
            private int checker;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WorkerInfoMonthDto {
            private String month;
            private int worker;
            private int checker;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkingAmount {
        private List<WorkingAmountDto> date = new ArrayList<>();
        private List<WorkingAmountDto> week = new ArrayList<>();
        private List<WorkingAmountMonthDto> month = new ArrayList<>();
        private List<WorkingAmountMonthDto> accumulation = new ArrayList<>();
        private Performance performance;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WorkingAmountDto {
            private String date;
            private int input;
            private int check;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WorkingAmountMonthDto {
            private String month;
            private int input;
            private int check;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Performance {
            private int total;
            private Long goal;
            private Long workingDay;
            private Long expectedWorkingDay;
            private String expectedFinishedDate;
        }
    }

}
