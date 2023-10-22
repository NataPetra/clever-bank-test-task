package by.nata.dao.api;

import by.nata.dao.entity.Bank;
import by.nata.dto.BankDto;

import java.util.List;

public interface IBankDao {

    void saveBank(Bank bank);

    BankDto getBankById(Long id);

    BankDto getBankByName(String name);

    List<BankDto> getAllBanks();

    void updateBank(Bank bank);

    void deleteBank(Long id);

}
