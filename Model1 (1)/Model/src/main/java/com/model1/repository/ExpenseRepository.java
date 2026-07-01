package main.java.com.model1.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import main.java.com.model1.model.entity.Expense;

public class ExpenseRepository {
    public List<Expense> findAll() throws SQLException {
        String sql = "SELECT expensedate, itemspurchased, expense, expensesector FROM expenses";
        List<Expense> expenses = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                expenses.add(mapExpense(resultSet));
            }
        }
        return expenses;
    }

    public int save(Connection connection, Expense expense) throws SQLException {
        String sql = "INSERT INTO expenses(expensedate, itemspurchased, expense, expenseSector) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, RepositoryMappers.formatLegacyDate(expense.date()));
            statement.setInt(2, expense.itemsPurchased());
            statement.setBigDecimal(3, expense.amount());
            statement.setInt(4, expense.sector());
            return statement.executeUpdate();
        }
    }

    public int totalItemsPurchased() throws SQLException {
        String sql = "SELECT SUM(itemspurchased) FROM expenses";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    public int totalItemsPurchasedByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT SUM(itemspurchased) FROM expenses WHERE expensedate>=? AND expensedate<=?";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, RepositoryMappers.formatLegacyDate(startDate));
            statement.setString(2, RepositoryMappers.formatLegacyDate(endDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public java.math.BigDecimal totalExpense() throws SQLException {
        String sql = "SELECT SUM(expense) AS totalexpense FROM expenses";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? RepositoryMappers.decimal(resultSet, "totalexpense") : java.math.BigDecimal.ZERO;
        }
    }

    public java.math.BigDecimal expenseByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT SUM(expense) AS totalexpense FROM expenses WHERE expensedate>=? AND expensedate<=?";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, RepositoryMappers.formatLegacyDate(startDate));
            statement.setString(2, RepositoryMappers.formatLegacyDate(endDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? RepositoryMappers.decimal(resultSet, "totalexpense") : java.math.BigDecimal.ZERO;
            }
        }
    }

    private Expense mapExpense(ResultSet resultSet) throws SQLException {
        LocalDate date = RepositoryMappers.legacyDate(resultSet, "expensedate");
        return new Expense(
                date,
                resultSet.getInt("itemspurchased"),
                RepositoryMappers.decimal(resultSet, "expense"),
                resultSet.getInt("expensesector"));
    }
}
