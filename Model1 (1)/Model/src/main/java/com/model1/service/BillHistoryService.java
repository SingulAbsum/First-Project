package main.java.com.model1.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import main.java.com.model1.exception.DatabaseException;
import main.java.com.model1.model.entity.Bill;
import main.java.com.model1.model.entity.BillItem;
import main.java.com.model1.repository.BillItemRepository;
import main.java.com.model1.repository.BillRepository;

public class BillHistoryService {
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;

    public BillHistoryService() {
        this(new BillRepository(), new BillItemRepository());
    }

    public BillHistoryService(BillRepository billRepository, BillItemRepository billItemRepository) {
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
    }

    public List<Bill> findAllBills() {
        try {
            return billRepository.findAll().stream()
                    .map(this::withItems)
                    .toList();
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to load bill history.", ex);
        }
    }

    public List<Bill> findBillsByDate(LocalDate date) {
        try {
            return billRepository.findByDate(date).stream()
                    .map(this::withItems)
                    .toList();
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to load bill history.", ex);
        }
    }

    private Bill withItems(Bill bill) {
        try {
            List<BillItem> items = billItemRepository.findByBillId(bill.id());
            return new Bill(
                    bill.id(),
                    bill.date(),
                    bill.total(),
                    bill.fileName(),
                    bill.cashierName(),
                    bill.totalItems(),
                    items);
        } catch (SQLException ex) {
            return bill;
        }
    }
}
