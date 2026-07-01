package main.java.com.model1.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import main.java.com.model1.model.entity.Administrator;
import main.java.com.model1.model.entity.Cashier;
import main.java.com.model1.model.entity.Employee;
import main.java.com.model1.model.entity.Manager;
import main.java.com.model1.model.enums.AccessLevel;
import main.java.com.model1.model.enums.Role;

public class EmployeeRepository {
    public int nextId(Role role) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(role);
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(table.nextIdSql());
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) + 1 : 1;
        }
    }

    public int nextId(Connection connection, Role role) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(role);
        try (PreparedStatement statement = connection.prepareStatement(table.nextIdSql());
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) + 1 : 1;
        }
    }

    public List<Employee> findByRole(Role role) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(role);
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(table.selectAllSql());
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                employees.add(mapEmployee(resultSet, table));
            }
        }
        return employees;
    }

    public Optional<Employee> findByName(Role role, String name) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(role);

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(table.selectByNameSql())) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapEmployee(resultSet, table));
                }
            }
        }
        return Optional.empty();
    }

    public int save(Connection connection, Employee employee) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(employee.getRole());
        try (PreparedStatement statement = connection.prepareStatement(table.insertSql())) {
            bindEmployee(statement, employee);
            return statement.executeUpdate();
        }
    }

    public int update(Connection connection, Employee employee) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(employee.getRole());
        try (PreparedStatement statement = connection.prepareStatement(table.updateSql())) {
            statement.setString(1, employee.getName());
            statement.setString(2, RepositoryMappers.formatLegacyDate(employee.getBirthDate()));
            statement.setString(3, employee.getPhoneNumber());
            statement.setString(4, employee.getEmail());
            statement.setBigDecimal(5, employee.getSalary());
            statement.setInt(6, employee.getAccessLevel().value());
            statement.setString(7, employee.getUsername());
            statement.setString(8, employee.getPassword());
            statement.setInt(9, employee.getSector());
            statement.setInt(10, employee.getId());
            return statement.executeUpdate();
        }
    }

    public int deleteByName(Connection connection, Role role, String name) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(role);
        try (PreparedStatement statement = connection.prepareStatement(table.deleteByNameSql())) {
            statement.setString(1, name);
            return statement.executeUpdate();
        }
    }

    public int deleteById(Connection connection, Role role, int id) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(role);
        try (PreparedStatement statement = connection.prepareStatement(table.deleteByIdSql())) {
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }

    public int updateSalary(Connection connection, Role role, String name, BigDecimal salary) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(role);
        try (PreparedStatement statement = connection.prepareStatement(table.updateSalarySql())) {
            statement.setBigDecimal(1, salary);
            statement.setString(2, name);
            return statement.executeUpdate();
        }
    }

    public int updateAccessLevel(Connection connection, Role role, String name, AccessLevel accessLevel) throws SQLException {
        EmployeeTable table = EmployeeTable.forRole(role);
        try (PreparedStatement statement = connection.prepareStatement(table.updateAccessLevelSql())) {
            statement.setInt(1, accessLevel.value());
            statement.setString(2, name);
            return statement.executeUpdate();
        }
    }

    public BigDecimal totalSalaryExpense() throws SQLException {
        String sql = """
                SELECT
                    (SELECT COALESCE(SUM(csalary), 0) FROM cashier) +
                    (SELECT COALESCE(SUM(msalary), 0) FROM manager) +
                    (SELECT COALESCE(SUM(asalary), 0) FROM administrator) AS salaryexpense
                """;
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? RepositoryMappers.decimal(resultSet, "salaryexpense") : BigDecimal.ZERO;
        }
    }

    private Employee mapEmployee(ResultSet resultSet, EmployeeTable table) throws SQLException {
        int id = resultSet.getInt(table.idColumn());
        String name = resultSet.getString(table.nameColumn());
        LocalDate birthDate = RepositoryMappers.legacyDate(resultSet, table.birthDateColumn());
        String phoneNumber = resultSet.getString(table.phoneColumn());
        String email = resultSet.getString(table.emailColumn());
        BigDecimal salary = RepositoryMappers.decimal(resultSet, table.salaryColumn());
        AccessLevel accessLevel = AccessLevel.fromValue(resultSet.getInt(table.accessLevelColumn()));
        String username = resultSet.getString(table.usernameColumn());
        String password = resultSet.getString(table.passwordColumn());
        int sector = resultSet.getInt("sector");

        return switch (table.role()) {
            case CASHIER -> new Cashier(id, name, birthDate, phoneNumber, email, salary, accessLevel, username, password, sector);
            case MANAGER -> new Manager(id, name, birthDate, phoneNumber, email, salary, accessLevel, username, password, sector);
            case ADMINISTRATOR -> new Administrator(id, name, birthDate, phoneNumber, email, salary, accessLevel, username, password, sector);
        };
    }

    private void bindEmployee(PreparedStatement statement, Employee employee) throws SQLException {
        statement.setInt(1, employee.getId());
        statement.setString(2, employee.getName());
        statement.setString(3, RepositoryMappers.formatLegacyDate(employee.getBirthDate()));
        statement.setString(4, employee.getPhoneNumber());
        statement.setString(5, employee.getEmail());
        statement.setBigDecimal(6, employee.getSalary());
        statement.setInt(7, employee.getAccessLevel().value());
        statement.setString(8, employee.getUsername());
        statement.setString(9, employee.getPassword());
        statement.setInt(10, employee.getSector());
    }

    private record EmployeeTable(
            Role role,
            String tableName,
            String idColumn,
            String nameColumn,
            String birthDateColumn,
            String phoneColumn,
            String emailColumn,
            String salaryColumn,
            String accessLevelColumn,
            String usernameColumn,
            String passwordColumn) {
        static EmployeeTable forRole(Role role) {
            return switch (role) {
                case CASHIER -> new EmployeeTable(
                        role, "cashier", "cid", "cname", "cbirthdate", "cphonenumber",
                        "email", "csalary", "caccesslevel", "cusername", "cpassword");
                case MANAGER -> new EmployeeTable(
                        role, "manager", "mid", "mname", "mbirthdate", "mphonenumber",
                        "memail", "msalary", "maccesslevel", "musername", "mpassword");
                case ADMINISTRATOR -> new EmployeeTable(
                        role, "administrator", "aid", "aname", "abirthdate", "aphonenumber",
                        "aemail", "asalary", "aaccesslevel", "ausername", "apassword");
            };
        }

        String selectAllSql() {
            return "SELECT * FROM " + tableName + " ORDER BY " + nameColumn;
        }

        String selectByNameSql() {
            return "SELECT * FROM " + tableName + " WHERE " + nameColumn + "=?";
        }

        String nextIdSql() {
            return "SELECT MAX(" + idColumn + ") FROM " + tableName;
        }

        String insertSql() {
            return "INSERT INTO " + tableName + "("
                    + idColumn + ", "
                    + nameColumn + ", "
                    + birthDateColumn + ", "
                    + phoneColumn + ", "
                    + emailColumn + ", "
                    + salaryColumn + ", "
                    + accessLevelColumn + ", "
                    + usernameColumn + ", "
                    + passwordColumn + ", sector) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        String updateSql() {
            return "UPDATE " + tableName + " SET "
                    + nameColumn + "=?, "
                    + birthDateColumn + "=?, "
                    + phoneColumn + "=?, "
                    + emailColumn + "=?, "
                    + salaryColumn + "=?, "
                    + accessLevelColumn + "=?, "
                    + usernameColumn + "=?, "
                    + passwordColumn + "=?, "
                    + "sector=? WHERE " + idColumn + "=?";
        }

        String deleteByNameSql() {
            return "DELETE FROM " + tableName + " WHERE " + nameColumn + "=?";
        }

        String deleteByIdSql() {
            return "DELETE FROM " + tableName + " WHERE " + idColumn + "=?";
        }

        String updateSalarySql() {
            return "UPDATE " + tableName + " SET " + salaryColumn + "=? WHERE " + nameColumn + "=?";
        }

        String updateAccessLevelSql() {
            return "UPDATE " + tableName + " SET " + accessLevelColumn + "=? WHERE " + nameColumn + "=?";
        }
    }
}
