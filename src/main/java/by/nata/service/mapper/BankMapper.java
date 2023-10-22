package by.nata.service.mapper;

import by.nata.dao.entity.Bank;
import by.nata.dto.BankDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankMapper {
    BankMapper INSTANCE = Mappers.getMapper(BankMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BankDto bankToBankDto(Bank bank);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "accounts", ignore = true)
    Bank bankDtoToBank(BankDto bankDto);
}
