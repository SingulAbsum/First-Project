package main.java.com.model1.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import main.java.com.model1.model.enums.AccessLevel;
import main.java.com.model1.model.enums.Role;

public abstract class Employee {
    private final int id;
    private final String name;
    private final LocalDate birthDate;
    private final String phoneNumber;
    private final String email;
    private final BigDecimal salary;
    private final AccessLevel accessLevel;
    private final String username;
    private final String password;
    private final int sector;
    private final Role role;

    protected Employee(
            int id,
            String name,
            LocalDate birthDate,
            String phoneNumber,
            String email,
            BigDecimal salary,
            AccessLevel accessLevel,
            String username,
            String password,
            int sector,
            Role role) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name");
        this.birthDate = Objects.requireNonNull(birthDate, "birthDate");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "phoneNumber");
        this.email = Objects.requireNonNull(email, "email");
        this.salary = Objects.requireNonNull(salary, "salary");
        this.accessLevel = Objects.requireNonNull(accessLevel, "accessLevel");
        this.username = Objects.requireNonNull(username, "username");
        this.password = Objects.requireNonNull(password, "password");
        this.sector = sector;
        this.role = Objects.requireNonNull(role, "role");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getSector() {
        return sector;
    }

    public Role getRole() {
        return role;
    }
}
