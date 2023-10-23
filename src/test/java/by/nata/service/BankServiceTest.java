package by.nata.service;

import by.nata.dao.api.IBankDao;
import by.nata.dao.entity.Bank;
import by.nata.dto.BankDto;
import by.nata.service.mapper.BankMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @InjectMocks
    private BankService bankService;

    @Mock
    private IBankDao bankDao;

    @Mock
    private BankMapper bankMapper;

    @Test
    void saveBank() {
        BankDto bankDto = new BankDto(1L, "Test Bank");
        Bank bankEntity = new Bank(1L, "Test Bank", null);

        when(bankMapper.bankDtoToBank(bankDto)).thenReturn(bankEntity);

        bankService.saveBank(bankDto);

        verify(bankMapper).bankDtoToBank(bankDto);
        verify(bankDao).saveBank(bankEntity);
    }

    @Test
    void saveBankWhenNameIsNull() {
        BankDto bankDto = new BankDto(null, null);

        bankService.saveBank(bankDto);

        verifyNoInteractions(bankMapper, bankDao);
    }

    @Test
    void getBankById() {
        Long bankId = 1L;
        BankDto expectedBank = new BankDto(bankId, "Test Bank");

        when(bankDao.getBankById(bankId)).thenReturn(expectedBank);

        BankDto result = bankService.getBankById(bankId);

        assertEquals(expectedBank, result);
    }

    @Test
    void getBankByIdWhenIdIsNull() {
        BankDto result = bankService.getBankById(null);

        assertNull(result);

        verifyNoInteractions(bankMapper, bankDao);
    }

    @Test
    void getAllBanks() {
        List<BankDto> expectedBanks = Arrays.asList(new BankDto(1L, "Bank1"), new BankDto(2L, "Bank2"));

        when(bankDao.getAllBanks()).thenReturn(expectedBanks);

        List<BankDto> result = bankService.getAllBanks();

        assertEquals(expectedBanks, result);
    }

    @Test
    void updateBank() {
        BankDto bankDto = new BankDto(1L, "Updated Bank");
        Bank bankEntity = new Bank(1L, "Updated Bank", null);

        when(bankMapper.bankDtoToBank(bankDto)).thenReturn(bankEntity);

        bankService.updateBank(bankDto);

        verify(bankMapper).bankDtoToBank(bankDto);
        verify(bankDao).updateBank(bankEntity);
    }

    @Test
    void updateBankWhenNameIsNull() {
        BankDto bankDto = new BankDto(null, null);

        bankService.updateBank(bankDto);

        verifyNoInteractions(bankMapper, bankDao);
    }

    @Test
    void deleteBank() {
        Long bankId = 1L;

        bankService.deleteBank(bankId);

        verify(bankDao).deleteBank(bankId);
    }

    @Test
    void deleteBankWhenIdIsNull() {
        bankService.deleteBank(null);

        verifyNoInteractions(bankMapper, bankDao);
    }
}