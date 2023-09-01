package by.nata.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "account_s_id", nullable = false)
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "account_b_id", nullable = false)
    private Account beneficiaryAccount;

    @Column(name = "transaction_date", nullable = false)
    private Date date;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionEnum type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!amount.equals(that.amount)) return false;
        if (!senderAccount.equals(that.senderAccount)) return false;
        if (!beneficiaryAccount.equals(that.beneficiaryAccount)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, senderAccount, beneficiaryAccount, date);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Transaction{");
        sb.append("id=").append(id);
        sb.append(", amount=").append(amount);
        sb.append(", senderAccount=").append(senderAccount);
        sb.append(", beneficiaryAccount=").append(beneficiaryAccount);
        sb.append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }
}
