package by.nata.dao.entity;

public enum TransactionEnum {

    REFILL("Пополнение"),
    WITHDRAWAL("Снятие средств"),
    TRANSFER("Перевод");

    private final String title;

    TransactionEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
