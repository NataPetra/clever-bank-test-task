package by.nata.service.util;

import by.nata.dao.entity.TransactionEnum;
import by.nata.dto.TransactionDto;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Log4j2
public class BankCheck {

    private BankCheck() {
        throw new IllegalStateException("Utility class");
    }

    @SneakyThrows
    public static void saveCheck(TransactionEnum transactionEnum, TransactionDto transactionDto) throws IOException {

        File dir = new File("../clever-bank-test-task/check");
        if (!dir.exists()) {
            dir.mkdir();
        }

        final Random random = SecureRandom.getInstanceStrong();
        int nextInt = random.nextInt(1000);
        String fileName = "../clever-bank-test-task/check/check" + nextInt + ".txt";
        String checkExample =
                """
                        -----------------------------------------
                        |           Банковский чек              |
                        | Чек:                    %13s |
                        | %s                   %s |
                        | Тип транзакции:            %13s |
                        | Банк отправителя:       %13s |
                        | Банк получателя:        %13s |
                        | Счет отправителя:       %13s |
                        | Счет получателя:        %13s |
                        | Сумма:                  %13s |
                        -----------------------------------------
                        """;


        String resultCheck = checkExample.formatted(
                String.valueOf(nextInt),
                LocalDate.now().toString(),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                transactionEnum.getTitle(),
                transactionDto.sBank(),
                transactionDto.bBank(),
                transactionDto.sAccount(),
                transactionDto.bAccount(),
                transactionDto.amount().toString()
        );

        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            log.info("Start writing check...");
            bufferedWriter.append(resultCheck);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
