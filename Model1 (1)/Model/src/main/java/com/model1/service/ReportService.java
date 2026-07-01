package main.java.com.model1.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import main.java.com.model1.exception.DatabaseException;
import main.java.com.model1.model.dto.CashierReport;
import main.java.com.model1.model.dto.FinancialReport;
import main.java.com.model1.model.dto.SalesReport;
import main.java.com.model1.repository.BillRepository;
import main.java.com.model1.repository.EmployeeRepository;
import main.java.com.model1.repository.ExpenseRepository;
import main.java.com.model1.util.ValidationUtils;

public class ReportService {
    private final BillRepository billRepository;
    private final ExpenseRepository expenseRepository;
    private final EmployeeRepository employeeRepository;

    public ReportService() {
        this(new BillRepository(), new ExpenseRepository(), new EmployeeRepository());
    }

    public ReportService(
            BillRepository billRepository,
            ExpenseRepository expenseRepository,
            EmployeeRepository employeeRepository) {
        this.billRepository = billRepository;
        this.expenseRepository = expenseRepository;
        this.employeeRepository = employeeRepository;
    }

    public CashierReport cashierReport(String cashierName, LocalDate startDate, LocalDate endDate) {
        ValidationUtils.requireNonBlank(cashierName, "Cashier name");
        ValidationUtils.requireDateRange(startDate, endDate);

        try {
            return new CashierReport(
                    cashierName,
                    startDate,
                    endDate,
                    billRepository.countByCashierAndDateRange(cashierName, startDate, endDate),
                    billRepository.totalItemsByCashierAndDateRange(cashierName, startDate, endDate),
                    billRepository.revenueByCashierAndDateRange(cashierName, startDate, endDate));
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to generate cashier report.", ex);
        }
    }

    public SalesReport salesReport(LocalDate startDate, LocalDate endDate) {
        ValidationUtils.requireDateRange(startDate, endDate);

        try {
            BigDecimal revenue = billRepository.revenueByDateRange(startDate, endDate);
            BigDecimal purchaseExpense = expenseRepository.expenseByDateRange(startDate, endDate);
            return new SalesReport(
                    startDate,
                    endDate,
                    billRepository.totalItemsSoldByDateRange(startDate, endDate),
                    expenseRepository.totalItemsPurchasedByDateRange(startDate, endDate),
                    revenue,
                    purchaseExpense,
                    revenue.subtract(purchaseExpense));
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to generate sales report.", ex);
        }
    }

    public FinancialReport financialReport(LocalDate startDate, LocalDate endDate) {
        ValidationUtils.requireDateRange(startDate, endDate);

        try {
            BigDecimal revenue = billRepository.revenueByDateRange(startDate, endDate);
            BigDecimal purchaseExpense = expenseRepository.expenseByDateRange(startDate, endDate);
            BigDecimal salaryExpense = employeeRepository.totalSalaryExpense();
            BigDecimal totalExpense = purchaseExpense.add(salaryExpense);
            return new FinancialReport(
                    startDate,
                    endDate,
                    revenue,
                    purchaseExpense,
                    salaryExpense,
                    totalExpense,
                    revenue.subtract(totalExpense));
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to generate financial report.", ex);
        }
    }
}
