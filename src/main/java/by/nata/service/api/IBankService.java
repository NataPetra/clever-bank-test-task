package by.nata.service.api;

import by.nata.dto.BankDto;

import java.util.List;

public interface IBankService {

    void saveBank(BankDto bank);

    BankDto getBankById(Long id);

    List<BankDto> getAllBanks();

    void updateBank(BankDto bank);

    void deleteBank(Long id);
}
