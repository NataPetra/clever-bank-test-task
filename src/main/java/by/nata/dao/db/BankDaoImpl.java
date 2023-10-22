package by.nata.dao.db;

import by.nata.dao.api.IBankDao;
import by.nata.dao.db.connection.api.IDataSourceWrapper;
import by.nata.dao.entity.Bank;
import by.nata.dto.BankDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.nata.dao.db.AccountDaoImpl.DATABASE_CONNECTION_ERROR;

public class BankDaoImpl implements IBankDao {

    private final IDataSourceWrapper dataSourceWrapper;

    public BankDaoImpl(IDataSourceWrapper dataSourceWrapper) {
        this.dataSourceWrapper = dataSourceWrapper;
    }

    @Override
    public void saveBank(Bank bank) {
        String insertQuery = "INSERT INTO clever_bank.bank (name) VALUES (?)";
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, bank.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_CONNECTION_ERROR, e);
        }
    }

    @Override
    public BankDto getBankById(Long id) {
        String selectQuery = "SELECT * FROM clever_bank.bank WHERE bank_id = ?";
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                return new BankDto(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BankDto getBankByName(String name) {
        String selectQuery = "SELECT * FROM clever_bank.bank WHERE name = ?";
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("bank_id");
                return new BankDto(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BankDto> getAllBanks() {
        List<BankDto> banks = new ArrayList<>();
        String selectQuery = "SELECT * FROM clever_bank.bank";
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("bank_id");
                String name = resultSet.getString("name");
                banks.add(new BankDto(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banks;
    }

    @Override
    public void updateBank(Bank bank) {
        String updateQuery = "UPDATE clever_bank.bank SET name = ? WHERE bank_id = ?";
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, bank.getName());
            statement.setLong(2, bank.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBank(Long id) {
        String deleteQuery = "DELETE FROM clever_bank.bank WHERE bank_id = ?";
        try (Connection connection = dataSourceWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
