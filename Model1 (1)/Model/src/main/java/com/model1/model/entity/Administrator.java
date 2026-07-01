package main.java.com.model1.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import main.java.com.model1.model.enums.AccessLevel;
import main.java.com.model1.model.enums.Role;

public final class Administrator extends Employee {
    public Administrator(
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
        super(id, name, birthDate, phoneNumber, email, salary, accessLevel, username, password, sector, Role.ADMINISTRATOR);
    }
}
