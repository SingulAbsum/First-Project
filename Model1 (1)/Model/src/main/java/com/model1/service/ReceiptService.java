package main.java.com.model1.service;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import main.java.com.model1.exception.ReceiptGenerationException;
import main.java.com.model1.model.entity.Bill;
import main.java.com.model1.model.entity.BillItem;
import main.java.com.model1.util.ValidationUtils;

public class ReceiptService {
    private static final DateTimeFormatter RECEIPT_DATE = DateTimeFormatter.ofPattern("MMddyyyy");
    private static final String CONFIG_RESOURCE = "app.properties";
    private static final String RECEIPTS_DIRECTORY_KEY = "receipts.directory";
    private static final String DEFAULT_RECEIPTS_DIRECTORY = "receipts";

    private final Path receiptDirectory;

    public ReceiptService() {
        this(configuredReceiptDirectory());
    }

    public ReceiptService(Path receiptDirectory) {
        this.receiptDirectory = receiptDirectory.toAbsolutePath().normalize();
    }

    public String createReceiptFileName() {
        return "modern-" + System.currentTimeMillis() + "-" + RECEIPT_DATE.format(LocalDate.now()) + ".txt";
    }

    public String storedReceiptPath(String receiptFileName) {
        ValidationUtils.requireNonBlank(receiptFileName, "Receipt file name");
        return receiptDirectory.getFileName().resolve(receiptFileName).toString();
    }

    public Path writeReceipt(Bill bill) {
        ValidationUtils.requireNonNull(bill, "Bill");

        try {
            Files.createDirectories(receiptDirectory);
            Path receiptPath = resolveReceiptPath(bill.fileName());
            Files.createDirectories(receiptPath.getParent());
            Files.write(receiptPath, renderReceipt(bill));
            return receiptPath;
        } catch (IOException ex) {
            throw new ReceiptGenerationException("Unable to write receipt file.", ex);
        }
    }

    public Path regenerateReceipt(Bill bill) {
        ValidationUtils.requireNonNull(bill, "Bill");
        ValidationUtils.requireNotEmpty(bill.items(), "Bill items");
        return writeReceipt(bill);
    }

    public void openReceipt(Bill bill) {
        ValidationUtils.requireNonNull(bill, "Bill");

        Path receiptPath = findReceiptPath(bill)
                .orElseThrow(() -> new ReceiptGenerationException("Receipt file is missing: " + bill.fileName(), null));
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            throw new ReceiptGenerationException("Opening receipt files is not supported on this machine.", null);
        }

        try {
            Desktop.getDesktop().open(receiptPath.toFile());
        } catch (IOException ex) {
            throw new ReceiptGenerationException("Unable to open receipt file.", ex);
        }
    }

    public Optional<Path> findReceiptPath(Bill bill) {
        ValidationUtils.requireNonNull(bill, "Bill");

        Path receiptPath = resolveReceiptPath(bill.fileName());
        if (Files.isRegularFile(receiptPath)) {
            return Optional.of(receiptPath);
        }

        Path legacyPath = Paths.get("").toAbsolutePath().resolve(bill.fileName()).normalize();
        if (Files.isRegularFile(legacyPath)) {
            return Optional.of(legacyPath);
        }
        return Optional.empty();
    }

    public List<String> renderReceipt(Bill bill) {
        ValidationUtils.requireNonNull(bill, "Bill");
        ValidationUtils.requireNotEmpty(bill.items(), "Bill items");

        List<String> lines = new java.util.ArrayList<>();
        lines.add("Bill #" + bill.id());
        lines.add("Date: " + bill.date());
        lines.add("Cashier: " + bill.cashierName());
        lines.add("");
        for (BillItem item : bill.items()) {
            lines.add(item.productName() + " "
                    + item.quantity() + "x"
                    + item.unitPrice().setScale(2, RoundingMode.HALF_UP)
                    + " Total:"
                    + item.lineTotal().setScale(2, RoundingMode.HALF_UP));
        }
        lines.add("");
        lines.add("Total: " + bill.total().setScale(2, RoundingMode.HALF_UP));
        return lines;
    }

    private Path resolveReceiptPath(String storedReceiptPath) {
        String value = ValidationUtils.requireNonBlank(storedReceiptPath, "Receipt path");
        Path path = Path.of(value);
        if (path.isAbsolute()) {
            return path.normalize();
        }
        if (path.getParent() != null) {
            return Paths.get("").toAbsolutePath().resolve(path).normalize();
        }
        return receiptDirectory.resolve(path).normalize();
    }

    private static Path configuredReceiptDirectory() {
        Properties properties = new Properties();
        try (InputStream input = ReceiptService.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            throw new ReceiptGenerationException("Unable to load receipt configuration.", ex);
        }

        String configuredDirectory = properties.getProperty(RECEIPTS_DIRECTORY_KEY, DEFAULT_RECEIPTS_DIRECTORY).trim();
        if (configuredDirectory.isBlank()) {
            configuredDirectory = DEFAULT_RECEIPTS_DIRECTORY;
        }

        Path path = Path.of(configuredDirectory);
        return path.isAbsolute() ? path : Paths.get("").toAbsolutePath().resolve(path);
    }
}
