package by.nata.service;

import by.nata.dao.api.IBankDao;
import by.nata.dao.entity.Bank;
import by.nata.dto.BankDto;
import by.nata.service.api.IBankService;
import by.nata.service.mapper.BankMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BankService implements IBankService {

    private final IBankDao bankDao;
    private final BankMapper bankMapper;

    @Override
    public void saveBank(BankDto bank) {
        Bank bankEntity = bankMapper.bankDtoToBank(bank);
        bankDao.saveBank(bankEntity);
    }

    @Override
    public BankDto getBankById(Long id) {
        return bankDao.getBankById(id);
    }

    @Override
    public List<BankDto> getAllBanks() {
        return bankDao.getAllBanks();
    }

    @Override
    public void updateBank(BankDto bank) {
        Bank bankEntity = bankMapper.bankDtoToBank(bank);
        bankDao.updateBank(bankEntity);
    }

    @Override
    public void deleteBank(Long id) {
        bankDao.deleteBank(id);
    }
}
