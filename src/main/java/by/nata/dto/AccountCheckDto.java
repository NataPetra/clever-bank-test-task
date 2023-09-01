package by.nata.dto;

public record AccountCheckDto(String senderBank, String beneficiaryBank,
                              String senderAccount, String beneficiaryAccount,
                              Double sum) {
}
