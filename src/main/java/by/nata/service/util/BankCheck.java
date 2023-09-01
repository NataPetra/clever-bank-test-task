package by.nata.service.util;

import by.nata.dao.entity.TransactionEnum;
import by.nata.dto.AccountCheckDto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class BankCheck {

    public static void saveCheck(TransactionEnum transactionEnum, AccountCheckDto accountCheckDto) throws IOException {

        File dir = new File("../clever-bank-test-task/check");
        if (!dir.exists()){
            dir.mkdir();
        }

        final Random random = new Random();
        int nextInt = random.nextInt(1000);
        String fileName="../clever-bank-test-task/check/check" + nextInt + ".txt";
        String checkExampleForTransfer =
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


        String resultCheckForTransfer = checkExampleForTransfer.formatted(
                String.valueOf(nextInt),
                LocalDate.now().toString(),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                transactionEnum.getTitle(),
                accountCheckDto.senderBank(),
                accountCheckDto.beneficiaryBank(),
                accountCheckDto.senderAccount(),
                accountCheckDto.beneficiaryAccount(),
                accountCheckDto.sum().toString()
        );

        String checkExample =
                """
                -----------------------------------------
                |           Банковский чек              |
                | Чек:                    %13s |
                | %s                   %s |
                | Тип транзакции:            %13s |
                | Банк получателя:        %13s |
                | Счет получателя:        %13s |
                | Сумма:                  %13s |
                -----------------------------------------
                """;


        String resultCheck = checkExample.formatted(
                String.valueOf(nextInt),
                LocalDate.now().toString(),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                transactionEnum.getTitle(),
                accountCheckDto.beneficiaryBank(),
                accountCheckDto.beneficiaryBank(),
                accountCheckDto.sum().toString()
        );

        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            if(transactionEnum.equals(TransactionEnum.TRANSFER)){
                bufferedWriter.append(resultCheckForTransfer);
            } else {
                bufferedWriter.append(resultCheck);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
