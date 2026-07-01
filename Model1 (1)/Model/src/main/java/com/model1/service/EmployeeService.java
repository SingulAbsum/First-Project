package main.java.com.model1.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import main.java.com.model1.exception.DatabaseException;
import main.java.com.model1.exception.ValidationException;
import main.java.com.model1.model.entity.Administrator;
import main.java.com.model1.model.entity.Cashier;
import main.java.com.model1.model.entity.Employee;
import main.java.com.model1.model.entity.Manager;
import main.java.com.model1.model.enums.AccessLevel;
import main.java.com.model1.model.enums.Role;
import main.java.com.model1.repository.DatabaseManager;
import main.java.com.model1.repository.EmployeeRepository;
import main.java.com.model1.util.ValidationUtils;

public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService() {
        this(new EmployeeRepository());
    }

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findByRole(Role role) {
        ValidationUtils.requireNonNull(role, "Role");

        try {
            return employeeRepository.findByRole(role);
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to load employees.", ex);
        }
    }

    public Optional<Employee> findByName(Role role, String name) {
        ValidationUtils.requireNonNull(role, "Role");
        ValidationUtils.requireNonBlank(name, "Employee name");

        try {
            return employeeRepository.findByName(role, name);
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to load employee.", ex);
        }
    }

    public int nextId(Role role) {
        ValidationUtils.requireNonNull(role, "Role");

        try {
            return employeeRepository.nextId(role);
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to allocate employee id.", ex);
        }
    }

    public Employee createEmployee(
            Role role,
            String name,
            LocalDate birthDate,
            String phoneNumber,
            String email,
            BigDecimal salary,
            AccessLevel accessLevel,
            String username,
            String password,
            int sector) {
        ValidationUtils.requireNonNull(role, "Role");

        return DatabaseManager.inTransaction(connection -> {
            int id = employeeRepository.nextId(connection, role);
            Employee employee = employee(role, id, name, birthDate, phoneNumber, email, salary, accessLevel, username, password, sector);
            validateEmployee(employee);
            employeeRepository.save(connection, employee);
            return employee;
        });
    }

    public void create(Employee employee) {
        validateEmployee(employee);

        DatabaseManager.inTransaction(connection -> {
            employeeRepository.save(connection, employee);
        });
    }

    public void update(Employee employee) {
        validateEmployee(employee);

        DatabaseManager.inTransaction(connection -> {
            int updated = employeeRepository.update(connection, employee);
            if (updated != 1) {
                throw new ValidationException("Employee not found for update: " + employee.getName());
            }
        });
    }

    public void delete(Role role, String name) {
        ValidationUtils.requireNonNull(role, "Role");
        ValidationUtils.requireNonBlank(name, "Employee name");

        DatabaseManager.inTransaction(connection -> {
            employeeRepository.deleteByName(connection, role, name);
        });
    }

    public void delete(Role role, int id) {
        ValidationUtils.requireNonNull(role, "Role");
        ValidationUtils.requirePositive(id, "Employee id");

        DatabaseManager.inTransaction(connection -> {
            int deleted = employeeRepository.deleteById(connection, role, id);
            if (deleted != 1) {
                throw new ValidationException("Employee not found for delete.");
            }
        });
    }

    public void updateSalary(Role role, String name, BigDecimal salary) {
        ValidationUtils.requireNonNull(role, "Role");
        ValidationUtils.requireNonBlank(name, "Employee name");
        ValidationUtils.requireNonNegative(salary, "Salary");

        DatabaseManager.inTransaction(connection -> {
            employeeRepository.updateSalary(connection, role, name, salary);
        });
    }

    public void updateAccessLevel(Role role, String name, AccessLevel accessLevel) {
        ValidationUtils.requireNonNull(role, "Role");
        ValidationUtils.requireNonBlank(name, "Employee name");
        ValidationUtils.requireNonNull(accessLevel, "Access level");

        DatabaseManager.inTransaction(connection -> {
            employeeRepository.updateAccessLevel(connection, role, name, accessLevel);
        });
    }

    public Employee employee(
            Role role,
            int id,
            String name,
            LocalDate birthDate,
            String phoneNumber,
            String email,
            BigDecimal salary,
            AccessLevel accessLevel,
            String username,
            String password,
            int sector) {
        return switch (role) {
            case CASHIER -> new Cashier(id, name, birthDate, phoneNumber, email, salary, accessLevel, username, password, sector);
            case MANAGER -> new Manager(id, name, birthDate, phoneNumber, email, salary, accessLevel, username, password, sector);
            case ADMINISTRATOR -> new Administrator(id, name, birthDate, phoneNumber, email, salary, accessLevel, username, password, sector);
        };
    }

    public AccessLevel expectedAccessLevel(Role role) {
        ValidationUtils.requireNonNull(role, "Role");
        return switch (role) {
            case CASHIER -> AccessLevel.CASHIER;
            case MANAGER -> AccessLevel.MANAGER;
            case ADMINISTRATOR -> AccessLevel.ADMINISTRATOR;
        };
    }

    private void validateEmployee(Employee employee) {
        ValidationUtils.requireNonNull(employee, "Employee");
        ValidationUtils.requireNonNull(employee.getRole(), "Role");
        ValidationUtils.requirePositive(employee.getId(), "Employee id");
        ValidationUtils.requireNonBlank(employee.getName(), "Employee name");
        ValidationUtils.requireNonNull(employee.getBirthDate(), "Birth date");
        ValidationUtils.requireNonBlank(employee.getPhoneNumber(), "Phone number");
        ValidationUtils.requireNonBlank(employee.getEmail(), "Email");
        ValidationUtils.requireNonNegative(employee.getSalary(), "Salary");
        ValidationUtils.requireNonNull(employee.getAccessLevel(), "Access level");
        ValidationUtils.requireNonBlank(employee.getUsername(), "Username");
        ValidationUtils.requireNonBlank(employee.getPassword(), "Password");
        ValidationUtils.requirePositive(employee.getSector(), "Sector");
        if (!employee.getEmail().contains("@")) {
            throw new ValidationException("Email must contain @.");
        }
        if (expectedAccessLevel(employee.getRole()) != employee.getAccessLevel()) {
            throw new ValidationException("Access level must match employee role.");
        }
    }
}
